package com.whut.onlinejudge.backgrounddoor.aop;


import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.whut.onlinejudge.backgrounddoor.constant.RedisConstant;
import com.whut.onlinejudge.backgrounddoor.utils.UserHolder;
import com.whut.onlinejudge.common.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author liuqiao
 * @since 2024-11-30
 * 根据请求中的 token 信息将用户对象信息缓存在处理流中,这种方式的缓存可以解决 MVC 原生 Interceptor 无法处理报错的情况
 */

@Aspect
@Order(2)
@AllArgsConstructor
@Component
@Slf4j
public class UserInfoAdvisor {

    private final StringRedisTemplate redisTemplate;

    @Before("execution(* com.whut.onlinejudge.backgrounddoor.controller..*.*(..))")
    public void setUserInfo() {
        final HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        final String token = request.getHeader("token");
        if (StrUtil.isBlank(token) || token.length() != 32)
            return;

        final String userKey = RedisConstant.USER_CACHE + token;
        final String userVoStr = redisTemplate.opsForValue().get(userKey);
        if (StrUtil.isBlank(userVoStr))
            return;

        final User user;
        try {
            user = JSONUtil.toBean(userVoStr, User.class);
        } catch (Throwable e) {
            return;
        }

        final String versionKey = RedisConstant.USER_LOGIN_VERSION_KEY + user.getId();
        final String version = redisTemplate.opsForValue().get(versionKey);
        if (!user.getVersion().toString().equals(version))
            return;

        redisTemplate.expire(userKey, RedisConstant.USER_CACHE_TIME, TimeUnit.MILLISECONDS);
        redisTemplate.expire(versionKey, RedisConstant.USER_LOGIN_VERSION_TIME, TimeUnit.MILLISECONDS);

        UserHolder.set(user);
    }

    @AfterReturning("execution(* com.whut.onlinejudge.backgrounddoor.controller..*.*(..))")
    public void remove() {
        UserHolder.remove();
        log.info("normal remove");
    }

    @AfterThrowing("execution(* com.whut.onlinejudge.backgrounddoor.controller..*.*(..))")
    public void removeAfterThrowing() {
        UserHolder.remove();
        log.info("exception remove");
    }

}
