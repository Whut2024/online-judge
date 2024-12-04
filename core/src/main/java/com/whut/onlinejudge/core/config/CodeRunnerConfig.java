package com.whut.onlinejudge.core.config;

import com.whut.onlinejudge.core.runner.RunnerEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liuqiao
 * @since 2024-12-02
 */
@ConfigurationProperties(prefix = "code-runner")
@Data
public class CodeRunnerConfig {

    private String pathPrefix;

    private RunnerEnum environment;

    private String machineId;
}
