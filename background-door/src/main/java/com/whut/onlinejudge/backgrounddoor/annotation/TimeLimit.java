package com.whut.onlinejudge.backgrounddoor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuqiao
 * @since 2024-12-05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface TimeLimit {

    /**
     * 资源 key 的前缀，会在后面拼接上当前用户的 ID 或者 IP 信息
     */
    String prefix() default "";

    /**
     * 锁能保持的时间(ms)
     */
    long time() default 500L;


    /**
     * 未登录时是否用 IP 限制
     */
    boolean importIp() default true;
}
