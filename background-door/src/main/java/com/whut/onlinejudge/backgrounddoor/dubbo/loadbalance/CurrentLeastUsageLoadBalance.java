package com.whut.onlinejudge.backgrounddoor.dubbo.loadbalance;

import cn.hutool.core.util.StrUtil;
import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author liuqiao
 * @since 2024-11-30
 */
@Slf4j
@Component
public class CurrentLeastUsageLoadBalance extends AbstractLoadBalance {

    public static StringRedisTemplate redisTemplate;

    private final static DefaultRedisScript<String> LOAD_BALANCE_SCRIPT;

    private final Random random = new Random();

    static {
        LOAD_BALANCE_SCRIPT = new DefaultRedisScript<>();
        LOAD_BALANCE_SCRIPT.setLocation(new ClassPathResource("redis-lua/load_balance.lua"));
        LOAD_BALANCE_SCRIPT.setResultType(String.class);
    }

    @SuppressWarnings("all")
    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokerList, URL x, Invocation invocation) {
        int failTime = 0;
        while (true) {
            final String member = redisTemplate.execute(LOAD_BALANCE_SCRIPT,
                    Collections.singletonList(RedisLoadBalanceConstant.MIN_HEAP_KEY),
                    String.valueOf(random.nextInt(100) * 0.01));
            if (StrUtil.isBlank(member) || RedisLoadBalanceConstant.EMPTY_RETURN.equals(member)) {
                log.error("负载均衡数据在Redis中缺失");
                if (++failTime == 3) {
                    throw new Error("负载均衡数据在Redis中缺失");
                }
            }

            for (Invoker<T> invoker : invokerList) {
                if (member.equals(invoker.getUrl().getParameter(RedisLoadBalanceConstant.MACHINE_ID_NAME))) {
                    log.info("member is {}", member);
                    return invoker;
                }
            }
            log.error("负载均衡实例在RPC配置中心缺失");
            if (++failTime == 3) {
                throw new Error("负载均衡实例在RPC配置中心缺失");
            }
        }
    }
}
