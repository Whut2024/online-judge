package com.whut.onlinejudge.core.runner.local;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.whut.onlinejudge.common.model.entity.JudgeCase;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.core.command.CommandFactory;
import com.whut.onlinejudge.core.config.CodeRunnerConfig;
import com.whut.onlinejudge.core.constant.CodeConstant;
import com.whut.onlinejudge.core.runner.CodeRunner;
import com.whut.onlinejudge.core.runner.CodeRunnerContext;
import com.whut.onlinejudge.core.runner.docker.DockerExecutor;
import com.whut.onlinejudge.core.util.LocalCodeUtil;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 直接在本地环境运行代码
 *
 * @author liuqiao
 * @since 2024-12-02
 */
@Component
@ConditionalOnMissingBean(DockerExecutor.class) // 兜底
@AllArgsConstructor
public class LocalCodeRunner extends CodeRunner {

    private final CodeRunnerConfig codeRunnerConfig;

    private final Runtime runtime = Runtime.getRuntime();

    @Override
    public JudgeInfo run(String language,
                         String submittedCode, String coreCode,
                         JudgeConfig judgeConfig, List<JudgeCase> judgeCaseList) {
        // 代码编译
        final String prefix = codeRunnerConfig.getPathPrefix() + File.separator + System.currentTimeMillis();

        if (LocalCodeUtil.compile(language, submittedCode, coreCode,
                prefix + CodeConstant.SOLUTION_NAME,
                prefix + CodeConstant.MAIN_NAME,
                prefix))
            // 编译失败
            return new JudgeInfo(0, 0, "编译失败");


        // 代码执行
        final CodeRunnerContext runnerContext = new CodeRunnerContext();
        final String command = CommandFactory.getExecutionCommand(language);
        if (command == null)
            return new JudgeInfo(0, 0, "编程语言错误");


        final Process process;
        try {
            process = runtime.exec(String.format(command, prefix, getInputArgs(judgeConfig, judgeCaseList)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            if (!process.waitFor(judgeConfig.getTimeLimit() * 3L / 2, TimeUnit.SECONDS)) {
                process.destroy();
                runnerContext.setException("time out");
                return this.extractContext(runnerContext);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 输出-没有异常-未通过
        // 输出-异常-有异常-未通过
        // 输出-内存-时间-通过
        final ArrayList<String> outputList = IoUtil.readLines(process.getInputStream(), StandardCharsets.UTF_8, new ArrayList<>());

        this.extractOutput(outputList, runnerContext);

        // 删除文件夹
        FileUtil.del(prefix);
        return this.extractContext(runnerContext);
    }
}
