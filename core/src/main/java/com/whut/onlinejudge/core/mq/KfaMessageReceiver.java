package com.whut.onlinejudge.core.mq;

import com.whut.onlinejudge.common.service.AnswerSubmissionResolveService;
import com.whut.onlinejudge.core.config.KfaConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * kafka 消息队列消息接收处理
 * @author liuqiao
 * @since 2025-03-24
 */
@Component
@Slf4j
@AllArgsConstructor
public class KfaMessageReceiver {

    private final AnswerSubmissionResolveService asrService;

    @KafkaListener(topics = KfaConfig.TOPIC, groupId = KfaConfig.GROUP_ID)
    public void submissionReceive(List<String> messageList) {
        for (String id : messageList) {
            asrService.resolve(Long.parseLong(id));
        }
    }
}
