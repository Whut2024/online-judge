package com.whut.onlinejudge.backgrounddoor.utils;


import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import com.whut.onlinejudge.backgrounddoor.exception.ThrowUtils;
import com.whut.onlinejudge.common.model.entity.User;

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
        final User user = USER_THREAD_LOCAL.get();
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_LOGIN_ERROR);
        return user;
    }

    public static User tryGet() {
        return USER_THREAD_LOCAL.get();
    }


    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }
}
