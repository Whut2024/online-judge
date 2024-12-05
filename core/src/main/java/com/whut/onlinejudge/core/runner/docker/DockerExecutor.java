package com.whut.onlinejudge.core.runner.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.whut.onlinejudge.core.config.CodeRunnerConfig;
import com.whut.onlinejudge.core.runner.RunnerEnum;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liuqiao
 * @since 2024-12-02
 */
@Slf4j
@Component
@Conditional(DockerExecutor.class)
public class DockerExecutor implements Condition {

    @Autowired
    private CodeRunnerConfig codeRunnerConfig;

    @SuppressWarnings("all")
    private final String OJ_IMAGE = "oj:v1";

    private final String CONTAINER_NAME = "oj";

    private static DockerClient dockerClient;

    private static String containerId;

    private static boolean check;

    private static boolean pass;

    @Override
    public boolean matches(@NotNull ConditionContext context, @NotNull AnnotatedTypeMetadata metadata) {
        if (check)
            return pass;

        RunnerEnum runnerEnum = context.getEnvironment().getProperty("code-runner.environment", RunnerEnum.class);
        if (!RunnerEnum.DOCKER.same(runnerEnum)) {
            pass = false;
            check = true;
            return false;
        }

        final List<Container> containerList;

        try {
            // 配置客户端
            dockerClient = DockerClientBuilder.getInstance().build();


            // 通信尝试
            containerList = dockerClient.listContainersCmd().exec();
        } catch (Exception e) {
            e.printStackTrace();
            pass = false;
            check = true;
            return false;
        }

        for (Container container : containerList) {
            if (container.getNames()[0].contains(CONTAINER_NAME)) {
                String id = container.getId();
                dockerClient.removeContainerCmd(id).withForce(true).exec();
            }
        }

        System.out.println(containerId);
        pass = true;
        check = true;
        return true;
    }

    @PostConstruct
    void init() {
        //创建容器
        final CreateContainerCmd containerCmd = dockerClient
                .createContainerCmd(OJ_IMAGE)
                .withName(CONTAINER_NAME);

        final HostConfig hostConfig = new HostConfig();
        hostConfig.withMemory(1000 * 1024 * 1024L)
                .withCpuCount(1L)
                .setBinds(new Bind(codeRunnerConfig.getPathPrefix(), new Volume(codeRunnerConfig.getPathPrefix())));

        // 执行创建命令 获取容器 ID
        containerId = containerCmd
                .withHostConfig(hostConfig)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .withTty(true)
                .exec().getId();

        //启动容器
        dockerClient.startContainerCmd(containerId).exec();
    }


    public List<String> execute(String command) {
        //执行命令并获取结果
        final String[] cmdArray = command.split(" ");
        final ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                .withCmd(cmdArray)
                .withAttachStderr(true)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .exec();

        final List<String> outputList = new ArrayList<>();
        @SuppressWarnings("all") final ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
            @Override
            public void onNext(Frame frame) {
                final String e = new String(frame.getPayload());
                if (!e.equals("\n"))
                    Collections.addAll(outputList, e.split("\n"));

                super.onNext(frame);
            }
        };

        try {
            dockerClient
                    .execStartCmd(execCreateCmdResponse.getId())
                    .exec(execStartResultCallback)
                    .awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        return outputList;
    }
}
