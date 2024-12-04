package com.whut.onlinejudge.backgrounddoor.judge;

import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.common.service.AnswerSubmissionResolveService;
import org.apache.dubbo.common.constants.LoadbalanceRules;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * @author liuqiao
 * @since 2024-12-01
 */
@ConditionalOnClass({AnswerSubmissionResolveService.class, DubboAutoConfiguration.class})
@Component
public class RpcRemoteJudgeStrategy implements JudgeStrategy {

    @DubboReference(retries = 0, loadbalance = "redis-least-usage", timeout = 10_000)
    private AnswerSubmissionResolveService answerSubmissionResolveService;


    @Override
    public JudgeInfo judge(Long asId) {
        return answerSubmissionResolveService.resolve(asId);
    }
}

