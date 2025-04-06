package com.whut.onlinejudge.core.runner;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.common.model.enums.RunnerStatusEnum;
import com.whut.onlinejudge.core.cache.CacheQuestion;
import com.whut.onlinejudge.core.command.CommandFactory;
import com.whut.onlinejudge.core.config.CodeRunnerConfig;
import com.whut.onlinejudge.core.util.LocalCodeUtil;
import com.whut.onlinejudge.core.util.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.List;

/**
 * 运行引导代码和用户提交的代码的类
 *
 * @author liuqiao
 * @since 2024-12-01
 */
@Slf4j
public abstract class CodeRunner {

    private final static String TRUE = "true";

    @Autowired
    private CodeRunnerConfig codeRunnerConfig;

    /**
     * @param language      编程语言
     * @param submittedCode 用户提交的代码
     * @return 运行过程中的异常和资源消耗
     */
    public final JudgeInfo run(String language, String submittedCode, CacheQuestion cq, Long id) {
        // 代码编译
        log.info("提交 {}, 代码编译", id);
        final String prefix = codeRunnerConfig.getPathPrefix()
                + File.separator + UserHolder.get(); // 避免并发冲突

        final String error = LocalCodeUtil.compileSubmittedCode(language, submittedCode, prefix);
        if (StrUtil.isNotBlank(error)) {
            // 编译失败
            FileUtil.del(prefix);
            final JudgeInfo compiledFailJudgeInfo = JudgeInfo.zeroLimit(RunnerStatusEnum.COMPILE_FAIL);
            compiledFailJudgeInfo.setMessage("");
            compiledFailJudgeInfo.setException(error);
            log.info("提交 {}, 代码编译失败", id);
            return compiledFailJudgeInfo;
        }


        // 代码执行
        log.info("提交 {}, 代码执行", id);
        final CodeRunnerContext runnerContext = new CodeRunnerContext();
        try {
            final String command = CommandFactory.getExecutionCommand(language, prefix, cq.getCompiledPath(language));
            if (StrUtil.isBlank(command))
                return JudgeInfo.zeroLimit(RunnerStatusEnum.LANGUAGE_ERROR);

            this.extractOutput(executeUsersCode(command, cq), runnerContext);
        } finally {
            // 删除文件夹
            FileUtil.del(prefix);
        }
        return this.getJudgeInfoFromContext(runnerContext);
    }


    /**
     * @param command invoke user's code command
     * @param cq      cache question
     * @return the output of use's code execution
     * <p>execute user's code and get output at list type</p>
     */
    protected abstract List<String> executeUsersCode(String command, CacheQuestion cq);

    /**
     * 处理代码结果返回
     */
    protected final JudgeInfo getJudgeInfoFromContext(CodeRunnerContext runnerContext) {
        final JudgeInfo judgeInfo = JudgeInfo.zeroLimit(RunnerStatusEnum.INNER);
        judgeInfo.setMessage(runnerContext.getOutput());
        judgeInfo.setMemory(runnerContext.getMemoryLimit());
        judgeInfo.setTime(runnerContext.getTimeLimit());
        judgeInfo.setException(runnerContext.getException());

        return judgeInfo;
    }

    /**
     * @param outputList // 输出-没有异常-未通过 // 输出-异常-有异常-未通过 // 输出-内存-时间-通过
     */
    protected final void extractOutput(List<String> outputList, CodeRunnerContext runnerContext) {
        final int size = outputList.size();
        final boolean pass = TRUE.equals(outputList.get(size - 1));
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
            if (TRUE.equals(outputList.get(size - 2))) {
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
            builder.append(outputList.get(i)).append(System.lineSeparator());
        }

        runnerContext.setOutput(builder.toString());
    }


}
