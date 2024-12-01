package com.whut.onlinejudge.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.onlinejudge.common.model.dto.answersubmission.AnswerSubmissionAddRequest;
import com.whut.onlinejudge.common.model.dto.answersubmission.AnswerSubmissionQueryRequest;
import com.whut.onlinejudge.common.model.entity.AnswerSubmission;
import com.whut.onlinejudge.common.model.vo.AnswerSubmissionVo;
import com.whut.onlinejudge.common.service.AnswerSubmissionService;
import com.whut.onlinejudge.core.mapper.AnswerSubmissionMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AnswerSubmissionServiceImpl extends ServiceImpl<AnswerSubmissionMapper, AnswerSubmission>
        implements AnswerSubmissionService {

    @Override
    public Long doQuestionSubmit(AnswerSubmissionAddRequest answerSubmissionAddRequest) {

        return 0L;
    }

    @Override
    public Page<AnswerSubmissionVo> listQuestionSubmitByPage(AnswerSubmissionQueryRequest answerSubmissionQueryRequest) {
        return null;
    }

}




