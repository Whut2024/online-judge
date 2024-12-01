package com.whut.onlinejudge.backgrounddoor.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import com.whut.onlinejudge.backgrounddoor.exception.ThrowUtils;
import com.whut.onlinejudge.backgrounddoor.mapper.QuestionMapper;
import com.whut.onlinejudge.backgrounddoor.utils.SqlUtils;
import com.whut.onlinejudge.backgrounddoor.utils.UserHolder;
import com.whut.onlinejudge.common.constant.CommonConstant;
import com.whut.onlinejudge.common.model.dto.question.QuestionAddRequest;
import com.whut.onlinejudge.common.model.dto.question.QuestionQueryRequest;
import com.whut.onlinejudge.common.model.dto.question.QuestionUpdateRequest;
import com.whut.onlinejudge.common.model.entity.JudgeCase;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.Question;
import com.whut.onlinejudge.common.model.entity.User;
import com.whut.onlinejudge.common.model.enums.UserRoleEnum;
import com.whut.onlinejudge.common.model.vo.QuestionVo;
import com.whut.onlinejudge.common.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        final String title = questionAddRequest.getTitle();
        final List<String> tags = questionAddRequest.getTags();
        final String content = questionAddRequest.getContent();
        final List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        final JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        final String coreCode = questionAddRequest.getCoreCode();

        final Question question = new Question();
        question.setUserId(UserHolder.get().getId());
        question.setTitle(title);
        question.setTags(JSONUtil.toJsonStr(tags));
        question.setContent(content);
        question.setJudgeCase(JSONUtil.toJsonStr(judgeCase));
        question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        question.setCoreCode(coreCode);
        this.save(question);

        return question.getId();
    }

    @Override
    public Boolean deleteQuestion(Long id) {
        // 查询出对应题目
        final LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getId, id);
        wrapper.select(Question::getUserId);
        final Long userId = this.getObj(wrapper, (o) -> (Long) o);
        ThrowUtils.throwIf(userId == null, ErrorCode.PARAMS_ERROR, "题目不存在");

        // 判断权限
        final User user = UserHolder.get();
        ThrowUtils.throwIf(!UserRoleEnum.isAdmin(user) && !user.getId().equals(userId), ErrorCode.NO_AUTH_ERROR);

        return this.removeById(id);
    }

    @Override
    public Question getQuestionById(Long id) {
        // 查询出对应题目
        final LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getId, id);
        final Question question = this.getOne(wrapper);
        ThrowUtils.throwIf(question == null, ErrorCode.PARAMS_ERROR, "题目不存在");

        // 判断权限
        final User user = UserHolder.get();
        ThrowUtils.throwIf(!UserRoleEnum.isAdmin(user) && !user.getId().equals(question.getUserId()), ErrorCode.NO_AUTH_ERROR);

        return question;
    }

    @Override
    public Page<Question> listQuestionByPage(QuestionQueryRequest questionQueryRequest) {
        final Page<Question> page = new Page<>(questionQueryRequest.getCurrent(), questionQueryRequest.getPageSize());

        return this.page(page, getQuestionQueryWrapper(questionQueryRequest));
    }

    @Override
    public Page<QuestionVo> listQuestionVoByPage(QuestionQueryRequest questionQueryRequest) {
        final Page<Question> page = new Page<>(questionQueryRequest.getCurrent(), questionQueryRequest.getPageSize());
        final Page<Question> questionPage = this.page(page, getQuestionQueryWrapper(questionQueryRequest));

        final Page<QuestionVo> questionVoPage = new Page<>(questionQueryRequest.getCurrent(), questionQueryRequest.getPageSize());
        questionVoPage.setTotal(questionPage.getTotal());

        List<QuestionVo> questionVoList = new ArrayList<>((int) questionPage.getSize());
        for (Question question : questionPage.getRecords()) {
            questionVoList.add(QuestionVo.getQuestionVo(question));
        }
        questionVoPage.setRecords(questionVoList);

        return questionVoPage;
    }

    private QueryWrapper<Question> getQuestionQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        final Long id = questionQueryRequest.getId();
        final Long userId = questionQueryRequest.getUserId();
        final String title = questionQueryRequest.getTitle();
        final List<String> tags = questionQueryRequest.getTags();
        final String content = questionQueryRequest.getContent();
        final Long endId = questionQueryRequest.getEndId();
        final String sortField = questionQueryRequest.getSortField();
        final String sortOrder = questionQueryRequest.getSortOrder();

        final QueryWrapper<Question> wrapper = new QueryWrapper<>();
        if (id != null) {
            wrapper.eq("id", id);
            return wrapper;
        }


        wrapper.eq(userId != null, "user_id", userId);
        wrapper.gt(endId != null, "id", endId);
        wrapper.like(StrUtil.isNotBlank(title), "title", title);
        wrapper.like(StrUtil.isNotBlank(content), "content", content);
        if (CollectionUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                wrapper.like(StrUtil.isNotBlank(tag), "tags", tag);
            }
        }

        wrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);

        return wrapper;
    }

    @Override
    public Boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest) {
        final Long id = questionUpdateRequest.getId();
        final String title = questionUpdateRequest.getTitle();
        final List<String> tags = questionUpdateRequest.getTags();
        final List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase();
        final JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
        final String content = questionUpdateRequest.getContent();
        final String coreCode = questionUpdateRequest.getCoreCode();


        // 查询出对应题目
        final LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getId, id);
        wrapper.select(Question::getUserId);
        final Long userId = this.getObj(wrapper, (o) -> (Long) o);
        ThrowUtils.throwIf(userId == null, ErrorCode.PARAMS_ERROR, "题目不存在");

        // 判断权限
        final User user = UserHolder.get();
        ThrowUtils.throwIf(!UserRoleEnum.isAdmin(user) && !user.getId().equals(userId), ErrorCode.NO_AUTH_ERROR);

        final Question question = new Question();
        question.setId(id);
        question.setTitle(title);
        if (CollectionUtil.isNotEmpty(tags))
            question.setTags(JSONUtil.toJsonStr(tags));
        if (CollectionUtil.isNotEmpty(judgeCase))
            question.setJudgeCase(JSONUtil.toJsonStr(judgeCase));
        if (judgeConfig != null)
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        question.setContent(content);
        question.setCoreCode(coreCode);

        return this.updateById(question);
    }
}




