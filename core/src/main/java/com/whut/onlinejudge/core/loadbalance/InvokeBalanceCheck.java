package com.whut.onlinejudge.core.loadbalance;

import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时检测有没有被调用，删除错误导致的异常负载量
 *
 * @author liuqiao
 * @since 2024-12-22
 */
@Slf4j
@Component
public class InvokeBalanceCheck {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${code-runner.machine-id}")
    private String machineId;


    private static long stamp = -1L;

    public static void update() {
        stamp = System.currentTimeMillis();
    }

    @Scheduled(fixedRate = 5000L, initialDelay = 60_000L)
    void check() {
        if (System.currentTimeMillis() - stamp < 5_000L)
            return;

        redisTemplate.opsForZSet().add(RedisLoadBalanceConstant.MIN_HEAP_KEY, machineId, 0D);
        log.info("machine id 为 {} 的机器 5 秒内没有被调用,负载已经被清零", machineId);
    }


}