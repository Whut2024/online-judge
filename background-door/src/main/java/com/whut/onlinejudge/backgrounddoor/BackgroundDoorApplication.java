package com.whut.onlinejudge.backgrounddoor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.whut.onlinejudge.backgrounddoor.mapper")
public class BackgroundDoorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackgroundDoorApplication.class, args);
    }

}
