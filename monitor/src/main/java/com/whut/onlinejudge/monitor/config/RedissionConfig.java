package com.whut.onlinejudge.monitor.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置 Redission 连接
 * @author whut2024
 * @since 2024-09-06
 */

@Configuration
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedissionConfig {


    private String host;


    private Integer port;


    private String password;


    private String username;


    private Integer database;


    @Bean
    public RedissonClient redissonClient() {
        //配置类
        final Config config = new Config();
        //添加Redis地址
        String redisAddress = String.format("redis://%s:%s", host, port);
        config.useSingleServer()
                .setAddress(redisAddress)
                .setUsername(username)
                .setPassword(password)
                .setDatabase(database);
        //创建Redisson客户端
        return Redisson.create(config);
    }
}
