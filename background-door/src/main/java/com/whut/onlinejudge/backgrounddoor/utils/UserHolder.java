package com.whut.onlinejudge.backgrounddoor.utils;


import com.whut.onlinejudge.common.entity.User;

/**
 * @author whut2024
 * @since 2024-09-01
 */
public class UserHolder {


    private final static ThreadLocal<User> USER_THREAD_LOCAL = new ThreadLocal<>();


    public static void set(User user) {
        USER_THREAD_LOCAL.set(user);
    }


    public static User get() {
        return USER_THREAD_LOCAL.get();
    }


    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }
}
