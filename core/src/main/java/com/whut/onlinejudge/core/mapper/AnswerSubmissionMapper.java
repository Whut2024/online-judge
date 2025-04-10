package com.whut.onlinejudge.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whut.onlinejudge.common.model.entity.AnswerSubmission;

/**
* @author laowang
* @description 针对表【t_answer_submission(答案提交表)】的数据库操作Mapper
* @createDate 2024-11-29 19:59:25
* @Entity com.whut.onlinejudge.common.model.entity.AnswerSubmission
*/
public interface AnswerSubmissionMapper extends BaseMapper<AnswerSubmission> {

    AnswerSubmission getSubmittedCodeLanguageQuestionId(Long asId);
}




