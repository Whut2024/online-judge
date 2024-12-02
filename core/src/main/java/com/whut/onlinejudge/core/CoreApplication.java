package com.whut.onlinejudge.core;

import com.whut.onlinejudge.core.config.CodeRunnerConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.whut.onlinejudge.core.mapper")
@EnableDubbo(scanBasePackages = {"com.whut.onlinejudge.core.service.impl"})
@EnableConfigurationProperties(CodeRunnerConfig.class)
public class CoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

}
