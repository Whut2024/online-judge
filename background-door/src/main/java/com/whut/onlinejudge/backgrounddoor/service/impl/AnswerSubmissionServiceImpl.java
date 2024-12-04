package com.whut.onlinejudge.backgrounddoor.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import com.whut.onlinejudge.backgrounddoor.dubbo.loadbalance.CurrentLeastUsageLoadBalance;
import com.whut.onlinejudge.backgrounddoor.exception.ThrowUtils;
import com.whut.onlinejudge.backgrounddoor.judge.JudgeStrategy;
import com.whut.onlinejudge.backgrounddoor.mapper.AnswerSubmissionMapper;
import com.whut.onlinejudge.backgrounddoor.utils.SqlUtils;
import com.whut.onlinejudge.backgrounddoor.utils.UserHolder;
import com.whut.onlinejudge.common.constant.CommonConstant;
import com.whut.onlinejudge.common.model.dto.answersubmission.AnswerSubmissionAddRequest;
import com.whut.onlinejudge.common.model.dto.answersubmission.AnswerSubmissionQueryRequest;
import com.whut.onlinejudge.common.model.entity.AnswerSubmission;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.common.model.entity.Question;
import com.whut.onlinejudge.common.model.entity.User;
import com.whut.onlinejudge.common.model.enums.SatusEnum;
import com.whut.onlinejudge.common.model.enums.UserRoleEnum;
import com.whut.onlinejudge.common.model.vo.AnswerSubmissionVo;
import com.whut.onlinejudge.common.service.AnswerSubmissionService;
import com.whut.onlinejudge.common.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class AnswerSubmissionServiceImpl extends ServiceImpl<AnswerSubmissionMapper, AnswerSubmission>
        implements AnswerSubmissionService {

    private final QuestionService questionService;

    private final JudgeStrategy judgeStrategy;

    private final StringRedisTemplate redisTemplate;

    @PostConstruct
    void init() {
        CurrentLeastUsageLoadBalance.redisTemplate = redisTemplate;
    }



    @Override
    public Long doQuestionSubmit(AnswerSubmissionAddRequest answerSubmissionAddRequest) {
        final Long questionId = answerSubmissionAddRequest.getQuestionId();
        final String submittedCode = answerSubmissionAddRequest.getSubmittedCode();
        final String language = answerSubmissionAddRequest.getLanguage();
        final Long userId = UserHolder.get().getId();

        // 题目校验
        final LambdaQueryWrapper<Question> questionWrapper = new LambdaQueryWrapper<>();
        questionWrapper.eq(Question::getId, questionId);
        ThrowUtils.throwIf(!questionService.getBaseMapper().exists(questionWrapper),
                ErrorCode.PARAMS_ERROR, "题目不存在");

        // 答案提交保存
        final AnswerSubmission as = new AnswerSubmission();
        as.setQuestionId(questionId);
        as.setSubmittedCode(submittedCode);
        as.setLanguage(language);
        as.setStatus(SatusEnum.RUNNING.getValue());
        as.setUserId(userId);
        this.save(as);

        final JudgeInfo judgeInfo = judgeStrategy.judge(as.getId());

        return 0L;
    }

    @Override
    public Page<AnswerSubmissionVo> listQuestionSubmitByPage(AnswerSubmissionQueryRequest answerSubmissionQueryRequest) {
        final Page<AnswerSubmission> page = this.page(new Page<>(answerSubmissionQueryRequest.getCurrent(),
                answerSubmissionQueryRequest.getPageSize()), getAnswerSubmissionWrapper(answerSubmissionQueryRequest));

        final List<AnswerSubmissionVo> answerSubmissionVoList = new ArrayList<>((int) page.getSize());
        for (AnswerSubmission answerSubmission : page.getRecords()) {
            answerSubmissionVoList.add(AnswerSubmissionVo.getAnswerSubmissionVo(answerSubmission));
        }
        final Page<AnswerSubmissionVo> answerSubmissionVoPage = new Page<>(answerSubmissionQueryRequest.getCurrent(),
                answerSubmissionQueryRequest.getPageSize());
        answerSubmissionVoPage.setRecords(answerSubmissionVoList);

        return answerSubmissionVoPage;
    }

    private QueryWrapper<AnswerSubmission> getAnswerSubmissionWrapper(AnswerSubmissionQueryRequest answerSubmissionQueryRequest) {
        final Long userId = answerSubmissionQueryRequest.getUserId();
        final Long questionId = answerSubmissionQueryRequest.getQuestionId();
        final String language = answerSubmissionQueryRequest.getLanguage();
        final Integer status = answerSubmissionQueryRequest.getStatus();
        final String sortField = answerSubmissionQueryRequest.getSortField();
        final String sortOrder = answerSubmissionQueryRequest.getSortOrder();

        final QueryWrapper<AnswerSubmission> wrapper = new QueryWrapper<>();

        final User user = UserHolder.get();
        if (!UserRoleEnum.isAdmin(user))
            wrapper.eq("user_id", user.getId());
        else
            wrapper.eq(userId != null, "user_id", userId);

        wrapper.eq(questionId != null, "question_id", questionId);
        wrapper.eq(StrUtil.isNotBlank(language), "language", language);
        wrapper.eq(status != null, "status", status);

        wrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);

        return wrapper;
    }
}




