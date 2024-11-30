package com.whut.onlinejudge.backgrounddoor;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.whut.onlinejudge.backgrounddoor.mapper")
@EnableDubbo
public class BackgroundDoorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackgroundDoorApplication.class, args);
    }

}
