package com.whut.onlinejudge.core.runner;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import com.whut.onlinejudge.common.model.entity.JudgeCase;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.common.model.enums.RunnerStatusEnum;
import com.whut.onlinejudge.core.command.CommandFactory;
import com.whut.onlinejudge.core.config.CodeRunnerConfig;
import com.whut.onlinejudge.core.constant.JavaCodeConstant;
import com.whut.onlinejudge.core.util.LocalCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * 运行引导代码和用户提交的代码的类
 *
 * @author liuqiao
 * @since 2024-12-01
 */
@Slf4j
public abstract class CodeRunner {

    @Autowired
    private CodeRunnerConfig codeRunnerConfig;

    private final LongAdder counterAdder = new LongAdder();

    private final LongAdder timeAdder = new LongAdder();


    /**
     * @param language      编程语言
     * @param submittedCode 用户提交的代码
     * @param coreCode      引导代码
     * @param judgeConfig   运行现状条件
     * @param judgeCaseList 代码输入和输出
     * @return 运行过程中的异常和资源消耗
     */
    public final JudgeInfo run(String language, String submittedCode, String coreCode,
                               JudgeConfig judgeConfig, List<JudgeCase> judgeCaseList) {
        // 代码编译
        final String prefix = codeRunnerConfig.getPathPrefix() + File.separator + System.currentTimeMillis();

        if (!LocalCodeUtil.compile(language, submittedCode, coreCode,
                prefix + JavaCodeConstant.SOLUTION_NAME,
                prefix + JavaCodeConstant.MAIN_NAME,
                prefix + JavaCodeConstant.SOLUTION_CLASS_NAME,
                prefix)) {
            // 编译失败
            FileUtil.del(prefix);
            return JudgeInfo.zeroLimit(RunnerStatusEnum.COMPILE_FAIL);
        }


        // 代码执行
        final CodeRunnerContext runnerContext = new CodeRunnerContext();
        final String command = CommandFactory.getExecutionCommand(language);
        if (command == null)
            return JudgeInfo.zeroLimit(RunnerStatusEnum.LANGUAGE_ERROR);


        this.extractOutput(executeAndGetOutput(command, prefix, judgeConfig, judgeCaseList), runnerContext);

        // 减少负载记录
        redisTemplate.opsForZSet().incrementScore(RedisLoadBalanceConstant.MIN_HEAP_KEY, machineId, -1f);
        counterAdder.increment();
        if (runnerContext.getTimeLimit() != null)
            timeAdder.add(runnerContext.getTimeLimit());

        // 删除文件夹
        FileUtil.del(prefix);
        return this.extractContext(runnerContext);
    }

    /**
     * 运行代码获取程序输出
     */
    protected abstract List<String> executeAndGetOutput(String command, String prefix,
                                                        JudgeConfig judgeConfig, List<JudgeCase> judgeCaseList);

    /**
     * 处理代码结果返回
     */
    protected final JudgeInfo extractContext(CodeRunnerContext runnerContext) {
        final JudgeInfo judgeInfo = JudgeInfo.zeroLimit(RunnerStatusEnum.INNER);
        if (StrUtil.isBlank(runnerContext.getException())) {
            // 正常运行
            judgeInfo.setMemory(runnerContext.getMemoryLimit());
            judgeInfo.setTime(runnerContext.getTimeLimit());
            judgeInfo.setMessage(runnerContext.getOutput());
        } else
            judgeInfo.setMessage(runnerContext.getOutput() + "\n" + runnerContext.getException());

        return judgeInfo;
    }

    /**
     * @return 内存 时间 测试案例个数 测试案例
     */
    protected final String getInputArgs(JudgeConfig judgeConfig, List<JudgeCase> judgeCaseList) {
        StringBuilder args = new StringBuilder();
        args.append(judgeConfig.getMemoryLimit()); // 内存
        args.append(" ");
        args.append(judgeConfig.getTimeLimit()); // 时间
        if (CollectionUtil.isNotEmpty(judgeCaseList)) {
            args.append(" ");
            args.append(judgeCaseList.size()); // 个数
            // 测试案例
            if (StrUtil.isNotBlank(judgeCaseList.get(0).getOutput())) {
                for (JudgeCase judgeCase : judgeCaseList) {
                    args.append(" ");
                    args.append(judgeCase.getInput());
                    args.append(" ");
                    args.append(judgeCase.getOutput());
                }
            } else {
                for (JudgeCase judgeCase : judgeCaseList) {
                    args.append(" ");
                    args.append(judgeCase.getInput()); // 个数
                }
            }
        } else {
            args.append(" ");
            args.append(0);
        }

        return args.toString();
    }

    /**
     * @param outputList // 输出-没有异常-未通过 // 输出-异常-有异常-未通过 // 输出-内存-时间-通过
     */
    protected final void extractOutput(List<String> outputList, CodeRunnerContext runnerContext) {
        final int size = outputList.size();
        final boolean pass = JavaCodeConstant.TRUE.equals(outputList.get(size - 1));
        int outputSize = -1;
        if (pass) {
            // 通过
            runnerContext.setPass(true);
            runnerContext.setMemoryLimit(Integer.parseInt(outputList.get(size - 3)));
            runnerContext.setTimeLimit(Integer.parseInt(outputList.get(size - 2)));
            outputSize = size - 3;
        } else {
            // 没有通过
            if (JavaCodeConstant.TRUE.equals(outputList.get(size - 2))) {
                // 异常
                runnerContext.setException(outputList.get(size - 3));
                outputSize = size - 3;
            } else
                // 没有异常
                outputSize = size - 2;

        }
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < outputSize; i++) {
            builder.append(outputList.get(i)).append("\n");
        }
        runnerContext.setOutput(builder.toString());
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final static DefaultRedisScript<Void> LOGOUT_NODE_SCRIPT;

    private String machineId;

    static {
        LOGOUT_NODE_SCRIPT = new DefaultRedisScript<>();
        LOGOUT_NODE_SCRIPT.setLocation(new ClassPathResource("redis-lua/logout_node.lua"));
    }

    @PostConstruct
    void init() {
        machineId = codeRunnerConfig.getMachineId();
        // 注册当前服务器到 redis 的负载均衡 sorted_set 中
        final Boolean absent = redisTemplate.opsForZSet().addIfAbsent(RedisLoadBalanceConstant.MIN_HEAP_KEY, machineId, 0f);
        if (Boolean.FALSE.equals(absent)) {
            log.error("当前 machine id 已经存在");
            Runtime.getRuntime().exit(-1);
        }

        // 程序推出时删除节点
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            redisTemplate.execute(LOGOUT_NODE_SCRIPT,
                    Collections.singletonList(RedisLoadBalanceConstant.MIN_HEAP_KEY),
                    machineId);
            log.warn("处理请求数 {}", counterAdder.sum());
            log.warn("用户代码运行时间 {}", timeAdder.sum());
        }));
    }
}
