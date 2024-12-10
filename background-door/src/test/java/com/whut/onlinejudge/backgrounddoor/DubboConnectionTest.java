package com.whut.onlinejudge.backgrounddoor;

import com.whut.onlinejudge.common.service.AnswerSubmissionService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author liuqiao
 * @since 2024-11-30
 */
@SpringBootTest
public class DubboConnectionTest {

    @DubboReference(retries = 0, loadbalance = "redis-least-usage")
    private AnswerSubmissionService answerSubmissionService;

   // @Test
    void testConnection() {
        System.out.println("end");
    }
}
