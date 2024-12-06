package com.whut.onlinejudge.monitor.ping;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import com.whut.onlinejudge.common.service.PingService;
import com.whut.onlinejudge.monitor.dubbo.loadbalance.ServiceChooser;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 两个不同频率的定时方法
 * <p>
 * 整体上是线程安全的，但是当前这两个方法使用的是同一个只含有一个线程的线程池，可能性能不行，或许以后可以拆分为两个类，使用并发批量执行
 *
 * @author liuqiao
 * @since 2024-12-06
 */
@Slf4j
@Component
public class PingMonitor {

    @PostConstruct
    void init() {
        ServiceChooser.redisTemplate = redisTemplate;
    }

    @DubboReference(loadbalance = "service-chooser", retries = 0)
    private PingService pingService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    private static final DefaultRedisScript<Void> DOWN_NODE_SCRIPT;

    private static final DefaultRedisScript<Void> UP_NODE_SCRIPT;

    private final static ThreadLocal<String> ID_STORAGE;

    static {
        ID_STORAGE = new ThreadLocal<>();

        DOWN_NODE_SCRIPT = new DefaultRedisScript<>();
        DOWN_NODE_SCRIPT.setLocation(new ClassPathResource("redis-lua/down_node.lua"));

        UP_NODE_SCRIPT = new DefaultRedisScript<>();
        UP_NODE_SCRIPT.setLocation(new ClassPathResource("redis-lua/up_node.lua"));
    }

    private final ConcurrentHashSet<String> failMachineIdSet = new ConcurrentHashSet<>(16);

    public static String get() {
        return ID_STORAGE.get();
    }

    @Scheduled(initialDelay = 1000L, fixedRate = 5000L)
    void checkDown() {

        final Set<String> set = redisTemplate.opsForSet().members(RedisLoadBalanceConstant.TO_DOWN_MACHINE_ID_KEY);
        log.info("当前准备下线节点信息 {}", set);
        if (CollectionUtil.isEmpty(set)) return;

        for (String machineId : set) {
            final RLock lock = redissonClient.getLock(RedisLoadBalanceConstant.CHECK_FAIL_KEY + machineId);
            if (!lock.tryLock()) continue;

            ID_STORAGE.set(machineId);
            try {
                boolean fail;
                try {
                    fail = !RedisLoadBalanceConstant.MOCK_SIGN.equals(pingService.check());
                } catch (RpcException e) {
                    log.warn("{} 节点可能存在网络问题", machineId);
                    fail = true;
                }
                if (fail) {
                    // 加权 删除待下线信息
                    redisTemplate.execute(DOWN_NODE_SCRIPT,
                            CollectionUtil.newArrayList(RedisLoadBalanceConstant.MIN_HEAP_KEY,
                                    RedisLoadBalanceConstant.TO_DOWN_MACHINE_ID_KEY),
                            machineId, RedisLoadBalanceConstant.FAIL_HEIGHT);
                    log.warn("{} 节点加权 删除待下线信息", machineId);
                    // 定时监测 set
                    failMachineIdSet.add(machineId);
                }
            } finally {
                ID_STORAGE.remove();
                lock.unlock();
            }
        }

    }


    @Scheduled(initialDelay = 60 * 1000L, fixedRate = 60 * 1000L)
    @SuppressWarnings("all")
    void ping() {
        log.info("当前已经下线节点信息 {}", failMachineIdSet);
        if (CollectionUtil.isEmpty(failMachineIdSet)) return;

        for (String machineId : failMachineIdSet) {
            final Set<String> set = redisTemplate.opsForSet().members(RedisLoadBalanceConstant.TO_DOWN_MACHINE_ID_KEY);
            if (CollectionUtil.isEmpty(set)) return;

            final RLock lock = redissonClient.getLock(RedisLoadBalanceConstant.CHECK_FAIL_KEY + machineId);
            if (!lock.tryLock()) continue;

            ID_STORAGE.set(machineId);
            try {
                boolean wake = false;
                try {
                    wake = RedisLoadBalanceConstant.MOCK_SIGN.equals(pingService.check());
                } catch (RpcException e) {
                    log.warn("{} also fail", machineId);
                } catch (RuntimeException e) {
                    // 节点已经从注册中心下线
                    failMachineIdSet.remove(machineId);
                    return;
                }
                log.warn("{} 节点可能恢复正常", machineId);

                if (wake) {
                    // 减权
                    redisTemplate.execute(UP_NODE_SCRIPT,
                            CollectionUtil.newArrayList(RedisLoadBalanceConstant.MIN_HEAP_KEY),
                            machineId, RedisLoadBalanceConstant.FAIL_HEIGHT);
                    log.warn("{} 节点减权", machineId);
                    failMachineIdSet.remove(machineId);
                }
            } finally {
                ID_STORAGE.remove();
                lock.unlock();
            }
        }
    }
}
