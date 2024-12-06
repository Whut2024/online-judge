package com.whut.onlinejudge.monitor.dubbo.loadbalance;

import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import com.whut.onlinejudge.monitor.ping.PingMonitor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liuqiao
 * @since 2024-11-30
 */
@Slf4j
@Component
public class ServiceChooser extends AbstractLoadBalance {

    public static StringRedisTemplate redisTemplate;


    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokerList, URL x, Invocation invocation) {
        String machineId = PingMonitor.get();
        for (Invoker<T> invoker : invokerList) {
            if (machineId.equals(invoker.getUrl().getParameter(RedisLoadBalanceConstant.MACHINE_ID_NAME)))
                return invoker;
        }

        // 节点已经在注册中心下线 删除在 redis 中的信息
        redisTemplate.opsForSet().remove(RedisLoadBalanceConstant.TO_DOWN_MACHINE_ID_KEY, machineId);
        redisTemplate.opsForZSet().remove(RedisLoadBalanceConstant.MIN_HEAP_KEY, machineId);
        log.warn("{} 节点已经在注册中心下线 删除在 redis 中的信息", machineId);

        throw new RuntimeException();

    }
}
