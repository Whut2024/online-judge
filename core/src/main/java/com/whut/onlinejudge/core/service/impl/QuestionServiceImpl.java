package com.whut.onlinejudge.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.onlinejudge.common.model.dto.question.QuestionAddRequest;
import com.whut.onlinejudge.common.model.dto.question.QuestionQueryRequest;
import com.whut.onlinejudge.common.model.dto.question.QuestionUpdateRequest;
import com.whut.onlinejudge.common.model.entity.Question;
import com.whut.onlinejudge.common.model.vo.QuestionVo;
import com.whut.onlinejudge.common.service.QuestionService;
import com.whut.onlinejudge.core.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
 * @author laowang
 * @description 针对表【t_question(题目表)】的数据库操作Service实现
 * @createDate 2024-11-29 20:00:35
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
        implements QuestionService {


    @Override
    public Long addQuestion(QuestionAddRequest questionAddRequest) {
        return null;
    }

    @Override
    public Boolean deleteQuestion(Long id) {
        return null;
    }

    @Override
    public Question getQuestionById(Long id) {
        return null;
    }

    @Override
    public Page<Question> listQuestionByPage(QuestionQueryRequest questionQueryRequest) {
        return null;
    }

    @Override
    public Page<QuestionVo> listQuestionVoByPage(QuestionQueryRequest questionQueryRequest) {
        return null;
    }


    @Override
    public Boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest) {
        return null;
    }
}




