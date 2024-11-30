package com.whut.onlinejudge.backgrounddoor.lock;

import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * 分布式锁方法封装
 *
 * @author liuqiao
 * @since 2024-11-30
 */
@Component
@AllArgsConstructor
public class DistributedLockSupport {


    private final RedissonClient redissonClient;

    public void lock(String key, LockSuccessTask lockSuccessTask, LockFailTask lockFailTask, LockTaskExceptionHandler exceptionHandler) {
        final RLock lock = redissonClient.getLock(key);
        if (lock.tryLock()) {
            try {
                lockSuccessTask.execute();
            } catch (Exception e) {
                if (exceptionHandler != null)
                    exceptionHandler.handle(e);
                else
                    throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        } else
            lockFailTask.execute();
    }

    public void lock(String key, LockSuccessTask lockSuccessTask, LockFailTask lockFailTask) {
        lock(key, lockSuccessTask, lockFailTask, null);
    }
}
