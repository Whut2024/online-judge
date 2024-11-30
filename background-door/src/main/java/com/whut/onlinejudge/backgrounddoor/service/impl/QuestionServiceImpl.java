package com.whut.onlinejudge.backgrounddoor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.onlinejudge.backgrounddoor.mapper.QuestionMapper;
import com.whut.onlinejudge.common.model.entity.Question;
import com.whut.onlinejudge.common.service.QuestionService;
import org.springframework.stereotype.Service;

/**
* @author laowang
* @description 针对表【t_question(题目表)】的数据库操作Service实现
* @createDate 2024-11-29 20:00:35
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




