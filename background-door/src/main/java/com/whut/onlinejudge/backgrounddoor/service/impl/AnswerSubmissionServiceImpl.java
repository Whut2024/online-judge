package com.whut.onlinejudge.backgrounddoor.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import com.whut.onlinejudge.backgrounddoor.config.KfaConfig;
import com.whut.onlinejudge.backgrounddoor.constant.RedisConstant;
import com.whut.onlinejudge.backgrounddoor.exception.BusinessException;
import com.whut.onlinejudge.backgrounddoor.exception.ThrowUtils;
import com.whut.onlinejudge.backgrounddoor.mapper.AnswerSubmissionMapper;
import com.whut.onlinejudge.backgrounddoor.utils.DangerousWordCheck;
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
import com.whut.onlinejudge.common.model.vo.SimpleListAnswerSubmissionVo;
import com.whut.onlinejudge.common.service.AnswerSubmissionService;
import com.whut.onlinejudge.common.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;


@Service
@AllArgsConstructor
@Slf4j
public class AnswerSubmissionServiceImpl extends ServiceImpl<AnswerSubmissionMapper, AnswerSubmission>
        implements AnswerSubmissionService {

    private final QuestionService questionService;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final static LongAdder count = new LongAdder();


    @Override
    public Long doQuestionSubmit(AnswerSubmissionAddRequest answerSubmissionAddRequest) {
        final Long questionId = answerSubmissionAddRequest.getQuestionId();
        final String submittedCode = answerSubmissionAddRequest.getSubmittedCode();
        final String language = answerSubmissionAddRequest.getLanguage();
        final Long userId = UserHolder.get().getId();

        // 关键字检查
        if (DangerousWordCheck.check(language, submittedCode))
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "代码错误");

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

        // 发送到消息队列
        final Long id = as.getId();
        count.increment();
        kafkaTemplate.send(KfaConfig.TOPIC, count.intValue() % 2, "", String.valueOf(id));
        log.info("发送消息 {}", id);
        return id;
    }

    @Override
    public Page<SimpleListAnswerSubmissionVo> listQuestionSubmitByPage(AnswerSubmissionQueryRequest answerSubmissionQueryRequest) {
        final QueryWrapper<AnswerSubmission> wrapper = getAnswerSubmissionWrapper(answerSubmissionQueryRequest);
        wrapper.select("id", "language", "judge_info", "status", "question_id", "user_id", "create_time");
        final Page<AnswerSubmission> page = this.page(new Page<>(answerSubmissionQueryRequest.getCurrent(),
                answerSubmissionQueryRequest.getPageSize()), wrapper);

        final List<SimpleListAnswerSubmissionVo> answerSubmissionVoList = new ArrayList<>((int) page.getSize());
        for (AnswerSubmission answerSubmission : page.getRecords()) {
            answerSubmissionVoList.add(SimpleListAnswerSubmissionVo.getSimpleListAnswerSubmissionVo(answerSubmission));
        }
        final Page<SimpleListAnswerSubmissionVo> answerSubmissionVoPage = new Page<>(answerSubmissionQueryRequest.getCurrent(),
                answerSubmissionQueryRequest.getPageSize());
        answerSubmissionVoPage.setRecords(answerSubmissionVoList);
        answerSubmissionVoPage.setTotal(page.getTotal());

        return answerSubmissionVoPage;
    }

    @Override
    public JudgeInfo submitCheck(Long id) {
        final String judgeInfoStr = stringRedisTemplate.opsForValue().get(RedisConstant.JUDGE_INFO_PREFIX + id);
        if (StrUtil.isEmpty(judgeInfoStr)) {
            return JudgeInfo.DEFAULT_RUNNING_JUDGE;
        }

        return JSONUtil.toBean(judgeInfoStr, JudgeInfo.class);
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




