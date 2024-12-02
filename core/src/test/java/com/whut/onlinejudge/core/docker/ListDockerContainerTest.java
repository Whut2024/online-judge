package com.whut.onlinejudge.core.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.Arrays;
import java.util.List;

/**
 * @author liuqiao
 * @since 2024-12-02
 */
public class ListDockerContainerTest {
    public static void main(String[] args) {
        final DockerClient dockerClient = DockerClientBuilder.getInstance().build();


        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        List<Container> containerList = listContainersCmd.withShowAll(true).exec();
        for (Container container:containerList){
            System.out.println(Arrays.toString(container.getNames()));
            System.out.println(container);
        }
    }
}
