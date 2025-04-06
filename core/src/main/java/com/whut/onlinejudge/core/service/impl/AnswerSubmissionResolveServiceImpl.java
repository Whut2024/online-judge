package com.whut.onlinejudge.core.service.impl;

import cn.hutool.json.JSONUtil;
import com.whut.onlinejudge.common.model.entity.AnswerSubmission;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.common.model.enums.RunnerStatusEnum;
import com.whut.onlinejudge.common.model.enums.SatusEnum;
import com.whut.onlinejudge.common.service.AnswerSubmissionResolveService;
import com.whut.onlinejudge.core.cache.CacheQuestion;
import com.whut.onlinejudge.core.cache.LocalQuestionCache;
import com.whut.onlinejudge.core.constant.RedisConstant;
import com.whut.onlinejudge.core.mapper.AnswerSubmissionMapper;
import com.whut.onlinejudge.core.runner.CodeRunner;
import com.whut.onlinejudge.core.util.UserHolder;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 校验答案提交和相关题目 选择合适的提交处理方式
 *
 * @author liuqiao
 * @since 2024-12-01
 */
@AllArgsConstructor
@Service
public class AnswerSubmissionResolveServiceImpl implements AnswerSubmissionResolveService {

    private final AnswerSubmissionMapper asMapper;

    private final CodeRunner runner;

    private final LocalQuestionCache localQuestionCache;

    private final StringRedisTemplate redisTemplate;

    @Override
    @Async("oj-code-runner")
    public void resolve(Long asId) {
        // 校验相关答案提交
        final AnswerSubmission as = asMapper.getSubmittedCodeLanguageQuestionId(asId);

        if (as == null) {
            final JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setMessage("相关的答案提交不存在");
            judgeInfo.setTime(0);
            judgeInfo.setMemory(0);
            // 保存 redis
            redisTemplate.opsForValue().set(RedisConstant.JUDGE_INFO_PREFIX + asId,
                    JSONUtil.toJsonStr(judgeInfo),
                    RedisConstant.JUDGE_INFO_TTL, TimeUnit.MILLISECONDS);
            return;
        }
        as.setId(asId);

        // 缓存用户id
        UserHolder.set(as.getUserId());

        // 获取题目修改配件缓存信息
        final CacheQuestion cacheQuestion = localQuestionCache.get(as.getQuestionId(), as.getLanguage());

        // 校验相关题目
        if (cacheQuestion.getId() == null) {
            final JudgeInfo judgeInfo = JudgeInfo.zeroLimit(RunnerStatusEnum.QUESTION_NOT_EXIST);
            updateAnswerSubmission(as, judgeInfo, SatusEnum.ERROR);
            // 保存 redis
            redisTemplate.opsForValue().set(RedisConstant.JUDGE_INFO_PREFIX + asId,
                    JSONUtil.toJsonStr(judgeInfo),
                    RedisConstant.JUDGE_INFO_TTL, TimeUnit.MILLISECONDS);
            return;
        }

        // 代码执行  结果处理
        final JudgeInfo judgeInfo = runner.run(as.getLanguage(), as.getSubmittedCode(), cacheQuestion, asId);
        // 保存 redis
        redisTemplate.opsForValue().set(RedisConstant.JUDGE_INFO_PREFIX + asId,
                JSONUtil.toJsonStr(judgeInfo),
                RedisConstant.JUDGE_INFO_TTL, TimeUnit.MILLISECONDS);
        updateAnswerSubmission(as, judgeInfo, SatusEnum.OVER);
    }

    private void updateAnswerSubmission(AnswerSubmission as, JudgeInfo judgeInfo, SatusEnum satusEnum) {
        as.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        as.setStatus(satusEnum.getValue());
        as.setSubmittedCode(null);
        as.setLanguage(null);
        as.setQuestionId(null);
        asMapper.updateById(as);
    }
}
