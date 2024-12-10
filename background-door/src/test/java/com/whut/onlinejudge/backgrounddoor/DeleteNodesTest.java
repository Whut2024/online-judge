package com.whut.onlinejudge.backgrounddoor;

import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.StandardCharsets;

/**
 * @author liuqiao
 * @since 2024-12-10
 */
@SpringBootTest
public class DeleteNodesTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //@Test
    void test() {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (int i = 11; i < 110; i++) {
                connection.zSetCommands().zRem(RedisLoadBalanceConstant.MIN_HEAP_KEY.getBytes(StandardCharsets.UTF_8),
                        String.valueOf(i).getBytes(StandardCharsets.UTF_8));
            }
            return null;
        });
    }
}
