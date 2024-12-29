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
import org.springframework.data.redis.core.script.DefaultRedisScript;
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
public class OtherIpNoticeAdvisor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MailTemplate mailTemplate;

    private final static DefaultRedisScript<Long> NOTE_IP;

    private static final Set<String> selfIpSet;

    @Value("${spring.mail.username}")
    private String username;

    static {
        NOTE_IP = new DefaultRedisScript<>();
        NOTE_IP.setScriptText("if not redis.call('get', KEYS[1]) == nil then redis.call('expire', KEYS[1], ARGV[1]) return 0 end redis.call('setex', KEYS[1], ARGV[1], 1) return 1");
        NOTE_IP.setResultType(Long.class);
        /**
         * if not redis.call('get', KEYS[1]) == nil then
         *     redis.call('expire', KEYS[1], ARGV[1])
         *     return 0
         * end
         * redis.call('setex', KEYS[1], ARGV[1], 1)
         * return 1
         */

        selfIpSet = new HashSet<>(16);
        Collections.addAll(selfIpSet,
                "1.94.101.35",
                "113.57.237.22",
                "113.57.237.92",
                "189.1.244.220",
                "47.121.138.177",
                "127.0.0.1",
                "0:0:0:0:0:0:0:1");
    }

    @SuppressWarnings("all")
    @Before("execution(* com.whut.onlinejudge.backgrounddoor.controller..*.*(..))")
    public void check() {
        final String ip = NetUtils.getIpAddress(((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes()))
                .getRequest());
        if (selfIpSet.contains(ip))
            return;

        /* 这个版本的通知存在读写并发问题
        if (redisTemplate.opsForValue().get(key) == null) {
            try {
                mailTemplate.notice("有新人访问", ip);
            } catch (MessagingException e) {
                log.warn("IP 邮件发送错误");
            }
        }

        redisTemplate.opsForValue().set(key, "1", RedisConstant.OTHER_IP_TIME, TimeUnit.MILLISECONDS);*/

        if (redisTemplate.execute(NOTE_IP,
                Collections.singletonList(RedisConstant.OTHER_IP_KEY + ip), // key 1
                String.valueOf(TimeUnit.MILLISECONDS.toSeconds(RedisConstant.OTHER_IP_TIME)) // argv 1
        ) == 0) {
            // ip 已经存在
            return;
        }

        // ip 在这次调用中不存在，但是在刚刚的 Redis lua 脚本的执行中已经被添加
        try {
            mailTemplate.notice("有新人访问", ip);
        } catch (MessagingException e) {
            log.warn("IP 邮件发送错误");
        }

    }

}
