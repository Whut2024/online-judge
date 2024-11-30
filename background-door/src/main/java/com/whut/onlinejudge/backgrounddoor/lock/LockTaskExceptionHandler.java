package com.whut.onlinejudge.backgrounddoor.lock;

/**
 * @author liuqiao
 * @since 2024-11-30
 */
@FunctionalInterface
public interface LockTaskExceptionHandler {
    void handle(Throwable e);
}
