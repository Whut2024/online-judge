package com.whut.onlinejudge.backgrounddoor.aop;


import com.whut.onlinejudge.backgrounddoor.constant.RedisConstant;
import com.whut.onlinejudge.backgrounddoor.mail.MailTemplate;
import com.whut.onlinejudge.backgrounddoor.utils.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 通知网站被其他人访问
 */

@Slf4j
@Aspect
@Order(2)
@Component
public class MailNoticeAdvisor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MailTemplate mailTemplate;

    private static final Set<String> selfIpSet;

    @Value("${spring.mail.username}")
    private String username;

    static {
        selfIpSet = new HashSet<>(16);
        Collections.addAll(selfIpSet,
                "1.94.101.35",
                "113.57.237.22",
                "113.57.237.92",
                "189.1.244.220",
                "47.121.138.177",
                "0:0:0:0:0:0:0:1");
    }

    @Before("execution(* com.whut.onlinejudge.backgrounddoor.controller..*.*(..))")
    public void check() {
        final String ip = NetUtils.getIpAddress(((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes()))
                .getRequest());
        if (selfIpSet.contains(ip))
            return;

        final String key = RedisConstant.OTHER_IP_KEY + ip;
        if (redisTemplate.opsForValue().get(key) == null) {
            try {
                mailTemplate.notice("有新人访问", ip);
            } catch (MessagingException e) {
                log.warn("IP 邮件发送错误");
            }
        }

        redisTemplate.opsForValue().set(key, "1", RedisConstant.OTHER_IP_TIME, TimeUnit.MILLISECONDS);
    }

}
