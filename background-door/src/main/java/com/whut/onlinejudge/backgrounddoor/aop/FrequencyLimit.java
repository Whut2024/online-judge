package com.whut.onlinejudge.backgrounddoor.aop;

import com.whut.onlinejudge.backgrounddoor.annotation.TimeLimit;
import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import com.whut.onlinejudge.backgrounddoor.constant.RedisConstant;
import com.whut.onlinejudge.backgrounddoor.exception.BusinessException;
import com.whut.onlinejudge.backgrounddoor.lock.DistributedLockSupport;
import com.whut.onlinejudge.backgrounddoor.utils.NetUtils;
import com.whut.onlinejudge.backgrounddoor.utils.UserHolder;
import com.whut.onlinejudge.common.model.entity.User;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author liuqiao
 * @since 2024-12-05
 */
@Aspect
@Component
@AllArgsConstructor
public class FrequencyLimit {

    private final DistributedLockSupport lockSupport;

    @Around("@annotation(timeLimit)")
    public Object limit(ProceedingJoinPoint joinPoint, TimeLimit timeLimit) {
        final User user = UserHolder.tryGet();
        final String key;
        if (user == null)
            key = timeLimit.prefix() +
                    NetUtils.getIpAddress(((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes()))
                            .getRequest());
        else
            key = timeLimit.prefix() + user.getId();


        final Object[] result = new Object[1];
        lockSupport.lock(key, timeLimit.time(),
                () -> {
                    try {
                        result[0] = joinPoint.proceed();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                },
                () -> {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "请求频率过快");
                });
        return result[0];
    }

    @Around("execution(* com.whut.onlinejudge.backgrounddoor.controller..*.*(..))")
    public Object totalLimit(ProceedingJoinPoint joinPoint) {
        final User user = UserHolder.tryGet();
        final String key;
        if (user == null)
            key = RedisConstant.GLOBAL_LIMIT_KEY +
                    NetUtils.getIpAddress(((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes()))
                            .getRequest());
        else
            key = RedisConstant.GLOBAL_LIMIT_KEY + user.getId();


        final Object[] result = new Object[1];
        lockSupport.lock(key, RedisConstant.GLOBAL_LIMIT_TIME,
                () -> {
                    try {
                        result[0] = joinPoint.proceed();
                    } catch (Throwable e) {
                        throw (RuntimeException) e;
                    }

                },
                () -> {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "请求频率过快");
                });
        return result[0];
    }


}
