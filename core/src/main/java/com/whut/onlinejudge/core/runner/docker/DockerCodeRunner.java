package com.whut.onlinejudge.core.runner.docker;

import cn.hutool.core.io.FileUtil;
import com.whut.onlinejudge.common.model.entity.JudgeCase;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.core.command.CommandFactory;
import com.whut.onlinejudge.core.config.CodeRunnerConfig;
import com.whut.onlinejudge.core.constant.JavaCodeConstant;
import com.whut.onlinejudge.core.runner.CodeRunner;
import com.whut.onlinejudge.core.runner.CodeRunnerContext;
import com.whut.onlinejudge.core.util.LocalCodeUtil;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * 使用本地 Docker 服务器来运行代码
 *
 * @author liuqiao
 * @since 2024-12-01
 */
//@ConditionalOnBean
@Component
@AllArgsConstructor
@ConditionalOnBean(DockerExecutor.class)
public class DockerCodeRunner extends CodeRunner {

    private final CodeRunnerConfig codeRunnerConfig;

    private final DockerExecutor dockerExecutor;

    @Override
    public JudgeInfo run(String language, String submittedCode, String coreCode, JudgeConfig judgeConfig, List<JudgeCase> judgeCaseList) {
        // 代码编译
        final String prefix = codeRunnerConfig.getPathPrefix() + File.separator + System.currentTimeMillis();

        if (LocalCodeUtil.compile(language, submittedCode, coreCode,
                prefix + JavaCodeConstant.SOLUTION_NAME,
                prefix + JavaCodeConstant.MAIN_NAME,
                prefix))
            // 编译失败
            return JudgeInfo.zeroLimit("编译失败");

        final String command = CommandFactory.getExecutionCommand(language);
        if (command == null)
            return JudgeInfo.zeroLimit("编程语言错误");

        // Docker 运行代码
        final CodeRunnerContext runnerContext = new CodeRunnerContext();
        final String endCommand = String.format(command, prefix, getInputArgs(judgeConfig, judgeCaseList));


        // 输出-没有异常-未通过
        // 输出-异常-有异常-未通过
        // 输出-内存-时间-通过
        final List<String> outputList = dockerExecutor.execute(endCommand);

        this.extractOutput(outputList, runnerContext);

        // 删除文件夹
        FileUtil.del(prefix);
        return this.extractContext(runnerContext);
    }


}
