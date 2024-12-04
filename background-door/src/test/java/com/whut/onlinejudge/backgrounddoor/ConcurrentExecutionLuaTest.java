package com.whut.onlinejudge.backgrounddoor;

import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;

/**
 * @author liuqiao
 * @since 2024-12-04
 */
@Slf4j
@SpringBootTest
public class ConcurrentExecutionLuaTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void execute() {
        final DefaultRedisScript<String> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("redis-lua/load-balance.lua"));
        script.setResultType(String.class);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    log.info(redisTemplate.execute(script, Collections.singletonList(RedisLoadBalanceConstant.MIN_HEAP_KEY)));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }
}
