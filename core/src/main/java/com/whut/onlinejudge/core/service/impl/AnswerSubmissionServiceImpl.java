package com.whut.onlinejudge.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.onlinejudge.common.model.entity.AnswerSubmission;
import com.whut.onlinejudge.common.service.AnswerSubmissionService;
import com.whut.onlinejudge.core.mapper.AnswerSubmissionMapper;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author liuqiao
 * @since 2024-11-29
 */
@DubboService(parameters = {"id-number", "2"})
public class AnswerSubmissionServiceImpl extends ServiceImpl<AnswerSubmissionMapper, AnswerSubmission>
        implements AnswerSubmissionService {

    @Override
    public void test() {
        System.out.println("123");
    }
}
