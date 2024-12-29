package com.whut.onlinejudge.core.aop;

import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import com.whut.onlinejudge.core.loadbalance.InvokeBalanceCheck;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author liuqiao
 * @since 2024-12-23
 */
@Aspect
@Component
@Slf4j
public class LoadBalanceAdvisor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${code-runner.machine-id}")
    private String machineId;

    @PostConstruct
    void init() {
        // 注册当前服务器到 redis 的负载均衡 sorted_set 中
        final Boolean absent = redisTemplate.opsForZSet().addIfAbsent(RedisLoadBalanceConstant.MIN_HEAP_KEY, machineId, 0f);
        if (Boolean.FALSE.equals(absent)) {
            log.error("当前 machine id 已经存在,重新设置负载为 0");
            redisTemplate.opsForZSet().add(RedisLoadBalanceConstant.MIN_HEAP_KEY, machineId, 0f);
            //Runtime.getRuntime().exit(-1);
        }

        // 程序推出时删除节点
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            redisTemplate.opsForZSet().remove(RedisLoadBalanceConstant.MIN_HEAP_KEY, machineId);
            log.info("{} 已经从负载均衡队列中删除", machineId);
        }));
    }

    @Around("execution(* com.whut.onlinejudge.core.runner.CodeRunner.run(..))")
    public Object loadBalance(ProceedingJoinPoint joinPoint) {
        // 刷新负载时间
        InvokeBalanceCheck.update();

        // 运行用户代码
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            // 减少负载量
            redisTemplate.opsForZSet().incrementScore(RedisLoadBalanceConstant.MIN_HEAP_KEY, machineId, -1D);
        }

        return result;
    }
}
