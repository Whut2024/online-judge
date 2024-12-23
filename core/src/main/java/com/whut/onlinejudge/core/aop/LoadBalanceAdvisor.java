package com.whut.onlinejudge.core.aop;

import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import com.whut.onlinejudge.core.loadbalance.InvokeBalanceCheck;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

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

    private final static DefaultRedisScript<Void> LOGOUT_NODE_SCRIPT;

    @Value("code-runner.machineId")
    private String machineId;

    static {
        LOGOUT_NODE_SCRIPT = new DefaultRedisScript<>();
        LOGOUT_NODE_SCRIPT.setLocation(new ClassPathResource("redis-lua/logout_node.lua"));
    }

    @PostConstruct
    void init() {
        // 注册当前服务器到 redis 的负载均衡 sorted_set 中
        final Boolean absent = redisTemplate.opsForZSet().addIfAbsent(RedisLoadBalanceConstant.MIN_HEAP_KEY, machineId, 0f);
        if (Boolean.FALSE.equals(absent)) {
            log.error("当前 machine id 已经存在");
            Runtime.getRuntime().exit(-1);
        }

        // 程序推出时删除节点
        Runtime.getRuntime().addShutdownHook(new Thread(() -> redisTemplate.execute(LOGOUT_NODE_SCRIPT,
                Collections.singletonList(RedisLoadBalanceConstant.MIN_HEAP_KEY),
                machineId)));
    }

    @Around("execution(* com.whut.onlinejudge.core.runner.CodeRunner.run())")
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
