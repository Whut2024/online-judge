package com.whut.onlinejudge.core.util;

/**
 * @author liuqiao
 * @since 2025-04-05
 */
public class UserHolder {

    private final static ThreadLocal<Long> USER_ID_TL = new ThreadLocal<>();

    public static void set(Long id) {
        USER_ID_TL.set(id);
    }

    public static void remove() {
        USER_ID_TL.remove();
    }

    public static Long get() {
        return USER_ID_TL.get();
    }
}
