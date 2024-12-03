package com.whut.onlinejudge.core.runner.docker;

import com.whut.onlinejudge.common.model.entity.JudgeCase;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.core.runner.CodeRunner;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

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

    private final DockerExecutor dockerExecutor;

    @Override
    protected List<String> executeAndGetOutput(String command, String prefix,
                                               JudgeConfig judgeConfig, List<JudgeCase> judgeCaseList) {
        // 输出-没有异常-未通过
        // 输出-异常-有异常-未通过
        // 输出-内存-时间-通过
        return dockerExecutor.execute(String.format(command, prefix, getInputArgs(judgeConfig, judgeCaseList)));
    }
}
