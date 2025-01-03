package com.whut.onlinejudge.core.runner;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.whut.onlinejudge.common.model.entity.JudgeCase;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.common.model.enums.RunnerStatusEnum;
import com.whut.onlinejudge.core.cache.CacheQuestion;
import com.whut.onlinejudge.core.command.CommandFactory;
import com.whut.onlinejudge.core.config.CodeRunnerConfig;
import com.whut.onlinejudge.core.constant.JavaCodeConstant;
import com.whut.onlinejudge.core.util.LocalCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * @param language      编程语言
     * @param submittedCode 用户提交的代码
     * @return 运行过程中的异常和资源消耗
     * 这个方法要被代理增强，不能被 final 修饰
     */
    public JudgeInfo run(String language, String submittedCode, CacheQuestion cq) {
        // 代码编译
        final String prefix = codeRunnerConfig.getPathPrefix() + File.separator + System.currentTimeMillis();

        final String error = LocalCodeUtil.compile(language, submittedCode, prefix);
        if (StrUtil.isNotBlank(error)) {
            // 编译失败
            FileUtil.del(prefix);
            final JudgeInfo compiledFailJudgeInfo = JudgeInfo.zeroLimit(RunnerStatusEnum.COMPILE_FAIL);
            compiledFailJudgeInfo.setMessage("");
            compiledFailJudgeInfo.setException(error);
            return compiledFailJudgeInfo;
        }


        // 代码执行
        final CodeRunnerContext runnerContext = new CodeRunnerContext();
        final String command = CommandFactory.getExecutionCommand(language, prefix, cq.getCompiledPath(language));
        if (command == null)
            return JudgeInfo.zeroLimit(RunnerStatusEnum.LANGUAGE_ERROR);

        this.extractOutput(executeAndGetOutput(command, cq), runnerContext);

        // 删除文件夹
        FileUtil.del(prefix);
        return this.extractContext(runnerContext);
    }

    /**
     * 运行代码获取程序输出
     */
    protected abstract List<String> executeAndGetOutput(String command, CacheQuestion cq);

    /**
     * 处理代码结果返回
     */
    protected final JudgeInfo extractContext(CodeRunnerContext runnerContext) {
        final JudgeInfo judgeInfo = JudgeInfo.zeroLimit(RunnerStatusEnum.INNER);
        judgeInfo.setMessage(runnerContext.getOutput());
        judgeInfo.setMemory(runnerContext.getMemoryLimit());
        judgeInfo.setTime(runnerContext.getTimeLimit());
        judgeInfo.setException(runnerContext.getException());

        return judgeInfo;
    }

    /**
     * out 内存 时间 测试案例个数 测试案例
     */
    protected final List<String> getOutputList(JudgeConfig judgeConfig, List<JudgeCase> judgeCaseList) {
        final char newLine = '\n';
        List<String> args = new ArrayList<>(3 + judgeCaseList.size() * 2);
        args.add(String.valueOf(judgeConfig.getMemoryLimit()) + newLine); // 内存
        args.add(String.valueOf(judgeConfig.getTimeLimit()) + newLine); // 时间

        if (CollectionUtil.isEmpty(judgeCaseList)) {
            // 没有输入输出
            args.add("0" + newLine);
            return args;
        }

        args.add(String.valueOf(judgeCaseList.size()) + newLine); // 个数

        // 测试案例输入
        for (JudgeCase judgeCase : judgeCaseList) {
            final List<String> input = judgeCase.getInput();
            for (String s : input) {
                args.add(s + newLine);
            }
        }

        if (CollectionUtil.isNotEmpty(judgeCaseList.get(0).getOutput())) {
            // 存在输出比较
            for (JudgeCase judgeCase : judgeCaseList) {
                final List<String> out = judgeCase.getOutput();
                for (String s : out) {
                    args.add(s + newLine);
                }
            }
        }

        return args;
    }

    /**
     * @param outputList // 输出-没有异常-未通过 // 输出-异常-有异常-未通过 // 输出-内存-时间-通过
     */
    protected final void extractOutput(List<String> outputList, CodeRunnerContext runnerContext) {
        final int size = outputList.size();
        final boolean pass = JavaCodeConstant.TRUE.equals(outputList.get(size - 1));
        int outputSize = -1;

        // 设置异常为 ""
        runnerContext.setException("");
        if (pass) {
            // 通过
            runnerContext.setPass(true);
            runnerContext.setMemoryLimit(Integer.parseInt(outputList.get(size - 3)));
            runnerContext.setTimeLimit(Integer.parseInt(outputList.get(size - 2)));
            outputSize = size - 3;
        } else {
            runnerContext.setTimeLimit(0);
            runnerContext.setMemoryLimit(0);
            // 没有通过
            if (JavaCodeConstant.TRUE.equals(outputList.get(size - 2))) {
                // 异常
                runnerContext.setException(outputList.get(size - 3));
                outputSize = size - 3;
            } else {
                // 没有异常
                outputSize = size - 2;
            }
        }

        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < outputSize; i++) {
            builder.append(outputList.get(i)).append("\n");
        }

        runnerContext.setOutput(builder.toString());
    }


}
