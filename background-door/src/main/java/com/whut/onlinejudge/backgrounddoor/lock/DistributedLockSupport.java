package com.whut.onlinejudge.backgrounddoor.lock;

import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
        lock(key, -1, lockSuccessTask, lockFailTask, exceptionHandler);
    }

    public void lock(String key, LockSuccessTask lockSuccessTask, LockFailTask lockFailTask) {
        lock(key, lockSuccessTask, lockFailTask, null);
    }

    public void lock(String key, long leaseTime,
                     LockSuccessTask lockSuccessTask, LockFailTask lockFailTask, LockTaskExceptionHandler exceptionHandler) {
        final RLock lock = redissonClient.getLock(key);
        try {
            if (lock.tryLock(-1, leaseTime, TimeUnit.MILLISECONDS)) {
                try {
                    lockSuccessTask.execute();
                } catch (Throwable e) {
                    if (exceptionHandler != null)
                        exceptionHandler.handle(e);
                    else
                        throw e;
                } finally {
                    lock.unlock();
                }
            } else
                lockFailTask.execute();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void lock(String key, long leaseTime,
                     LockSuccessTask lockSuccessTask, LockFailTask lockFailTask) {
        lock(key, leaseTime, lockSuccessTask, lockFailTask, null);
    }
}
