package com.whut.onlinejudge.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.onlinejudge.common.model.dto.question.QuestionAddRequest;
import com.whut.onlinejudge.common.model.dto.question.QuestionQueryRequest;
import com.whut.onlinejudge.common.model.dto.question.QuestionUpdateRequest;
import com.whut.onlinejudge.common.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whut.onlinejudge.common.model.vo.QuestionVo;

/**
* @author laowang
* @description 针对表【t_question(题目表)】的数据库操作Service
* @createDate 2024-11-29 20:00:35
*/
public interface QuestionService extends IService<Question> {

    /**
     * 添加问题
     *
     * @param questionAddRequest 包含要添加问题的信息的请求对象
     * @return 添加问题后的唯一标识符
     */
    Long addQuestion(QuestionAddRequest questionAddRequest);

    /**
     * 删除问题
     *
     * @param id 要删除的问题的唯一标识符
     * @return 删除操作是否成功的布尔值
     */
    Boolean deleteQuestion(Long id);

    /**
     * 根据问题ID获取问题详情
     *
     * @param id 问题的唯一标识符
     * @return 对应ID的问题对象
     */
    Question getQuestionById(Long id);

    /**
     * 分页查询问题列表
     *
     * @param questionQueryRequest 包含查询条件和分页信息的请求对象
     * @return 符合查询条件的问题列表，包含分页信息
     */
    Page<Question> listQuestionByPage(QuestionQueryRequest questionQueryRequest);

    /**
     * 分页查询问题详情视图列表
     *
     * @param questionQueryRequest 包含查询条件和分页信息的请求对象
     * @return 符合查询条件的问题详情视图列表，包含分页信息
     */
    Page<QuestionVo> listQuestionVoByPage(QuestionQueryRequest questionQueryRequest);

    /**
     * 更新问题信息
     *
     * @param questionUpdateRequest 包含要更新问题信息的请求对象
     * @return 更新操作是否成功的布尔值
     */
    Boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest);

}
