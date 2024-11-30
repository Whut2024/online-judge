package com.whut.onlinejudge.backgrounddoor.constant;

/**
 * @author liuqiao
 * @since 2024-11-30
 */
public interface RedisConstant {

    String SALT = "14514";

    String USER_REGISTER_LOCK = "USER:REGISTER:LOCK:"; // 19 + 16 = 35


    String USER_CACHE = "USER:CACHE:"; // 11 + 32 = 43

    long USER_CACHE_TIME = 30 * 60 * 1000L;


    String USER_LOGIN_VERSION_KEY = "USER:LOGIN:VERSION:"; // 19 + 19 = 36

    long USER_LOGIN_VERSION_TIME = 30 * 60 * 1000L;
}
