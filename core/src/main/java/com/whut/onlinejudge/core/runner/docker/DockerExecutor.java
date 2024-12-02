package com.whut.onlinejudge.core.runner.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.core.DockerClientBuilder;
import com.whut.onlinejudge.core.runner.RunnerEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

/**
 * @author liuqiao
 * @since 2024-12-02
 */
@Component
@ConditionalOnProperty(prefix = "code-runner", name = "environment", havingValue = "docker")
@Conditional(DockerExecutor.class)
public class DockerExecutor implements Condition {

    private DockerClient dockerClient;

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (!RunnerEnum.DOCKER.same(context.getEnvironment().getProperty("code-runner.environment", RunnerEnum.class)))
            return false;

        // 配置客户端
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();

        // 通信尝试
        PingCmd pingCmd = dockerClient.pingCmd();
        pingCmd.exec();
        if (false) {
            return false;
        }

        return true;
    }
}
