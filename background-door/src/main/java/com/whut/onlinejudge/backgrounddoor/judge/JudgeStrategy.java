package com.whut.onlinejudge.backgrounddoor.judge;

import com.whut.onlinejudge.backgrounddoor.dubbo.filter.InvocationFailFilter;
import com.whut.onlinejudge.backgrounddoor.dubbo.loadbalance.CurrentLeastUsageLoadBalance;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;

/**
 * @author liuqiao
 * @since 2024-12-01
 */
public abstract class JudgeStrategy {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    void init() {
        CurrentLeastUsageLoadBalance.redisTemplate = redisTemplate;
        InvocationFailFilter.redisTemplate = redisTemplate;
    }

    /**
     * 实现从数据库获取相关的答案提交信息 将提交信息里面用户编写的代码配合上引导代码运行 处理运行结果 返回结果
     *
     * @param asId 用户的答案提交 ID
     * @return 答案提交代码运行处理的结果
     */
    public abstract JudgeInfo judge(Long asId);
}
