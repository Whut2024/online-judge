package com.whut.onlinejudge.core.runner;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 代码执行的环境枚举
 * @author liuqiao
 * @since 2024-12-02
 */
@AllArgsConstructor
@Getter
public enum RunnerEnum {

    DOCKER("docker"),
    LOCAL("local");

    private String name;


    public boolean same(RunnerEnum runnerEnum) {
        return runnerEnum != null &&  DOCKER.name.equals(runnerEnum.name);
    }
}
