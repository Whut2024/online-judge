package com.whut.onlinejudge.core.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;

public class DockerDemo {
    public static void main(String[] args) throws InterruptedException {
        //获取默认的Docker Client
        final DockerClient dockerClient = DockerClientBuilder.getInstance().build();
//        PingCmd pingCmd=dockerClient.pingCmd();
//        pingCmd.exec();
        final String image = "nacos/nacos-server:v2.4.3";
        final PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
        final PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
            @Override
            public void onNext(PullResponseItem item) {
                System.out.println("下载镜像" + item.getStatus());
                super.onNext(item);
            }
        };
        pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
        System.out.println("下载完成");
    }
}