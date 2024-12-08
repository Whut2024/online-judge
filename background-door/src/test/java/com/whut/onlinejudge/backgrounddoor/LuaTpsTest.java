package com.whut.onlinejudge.backgrounddoor;

import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author liuqiao
 * @since 2024-12-08
 */
@Slf4j
@SpringBootTest
public class LuaTpsTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final static DefaultRedisScript<String> SCRIPT;

    private final static Random random = new Random();

    static {
        SCRIPT = new DefaultRedisScript<>();
        SCRIPT.setLocation(new ClassPathResource("redis-lua/load_balance.lua"));
        SCRIPT.setResultType(String.class);
    }

    @Test
    void test() {

        final LongAdder counter = new LongAdder();
        final long start = System.currentTimeMillis();

        for (int i = 0; i < 20; i++) {
            createNodes(100);
            new Thread(() -> {
                while (true) {
                    invoke();
                }
            }).start();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            final long timeCost = System.currentTimeMillis() - start;
            final long sum = counter.sum();
            log.warn("time is {}, counter is {} and TPS is {}", timeCost, sum, sum * 1000.0 / timeCost);
        }));

        try {
            Thread.sleep(100000000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    void createNodes(int number) {
        for (int i = 10; i < number + 10; i++) {
            redisTemplate.opsForZSet().add(RedisLoadBalanceConstant.MIN_HEAP_KEY, String.valueOf(i), 0D);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (int i = 10; i < number + 10; i++) {
                redisTemplate.opsForZSet().remove(RedisLoadBalanceConstant.MIN_HEAP_KEY, String.valueOf(i));
            }
        }));
    }

    void invoke() {
        final long start = System.currentTimeMillis();
        final String id = redisTemplate.execute(SCRIPT,
                Collections.singletonList(RedisLoadBalanceConstant.MIN_HEAP_KEY),
                String.valueOf(random.nextInt(100) * 0.01));
        log.info("id is {} and the invocation cost {}", id, System.currentTimeMillis() - start);


        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assert id != null;
        redisTemplate.opsForZSet().incrementScore(RedisLoadBalanceConstant.MIN_HEAP_KEY, id, -1D);
    }

}
