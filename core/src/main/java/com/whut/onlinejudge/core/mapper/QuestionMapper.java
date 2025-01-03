package com.whut.onlinejudge.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whut.onlinejudge.common.model.entity.Question;

/**
* @author laowang
* @description 针对表【t_question(题目表)】的数据库操作Mapper
* @createDate 2024-11-29 20:00:35
* @Entity com.whut.onlinejudge.common.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {
    Question getCoreCodeBaseCodeJudgeCaseJudgeConfig(Long id);
}




