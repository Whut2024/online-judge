package com.whut.onlinejudge.core.runner.docker;

import com.whut.onlinejudge.common.model.entity.JudgeCase;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.core.config.CodeRunnerConfig;
import com.whut.onlinejudge.core.constant.CodeConstant;
import com.whut.onlinejudge.core.runner.CodeRunner;
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

    @Override
    public JudgeInfo run(String submittedCode, String coreCode, JudgeConfig judgeConfig, List<JudgeCase> judgeCaseList) {
        // 代码编译
        final String prefix = codeRunnerConfig.getPathPrefix() + File.separator + System.currentTimeMillis();

        if (!LocalCodeUtil.compile(submittedCode, coreCode,
                prefix + CodeConstant.SOLUTION_NAME,
                prefix + CodeConstant.MAIN_NAME,
                prefix))
            // 编译失败
            return new JudgeInfo(0, 0, "编译失败");


        // Docker 运行代码

        // 运行结果(返回值 消耗资源)返回

        final JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemory(10);
        judgeInfo.setMessage("OK");
        judgeInfo.setTime(10);
        return judgeInfo;
    }
}
