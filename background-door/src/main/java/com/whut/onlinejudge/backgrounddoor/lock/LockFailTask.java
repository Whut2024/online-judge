package com.whut.onlinejudge.backgrounddoor.lock;

/**
 * 加锁失败后的操作
 * @author liuqiao
 * @since 2024-11-30
 */
@FunctionalInterface
public interface LockFailTask {

    void execute();
}
