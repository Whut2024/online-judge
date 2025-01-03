package com.whut.onlinejudge.core.service.impl;

import cn.hutool.json.JSONUtil;
import com.whut.onlinejudge.common.model.entity.AnswerSubmission;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.common.model.enums.RunnerStatusEnum;
import com.whut.onlinejudge.common.model.enums.SatusEnum;
import com.whut.onlinejudge.common.service.AnswerSubmissionResolveService;
import com.whut.onlinejudge.common.service.QuestionService;
import com.whut.onlinejudge.core.cache.CacheQuestion;
import com.whut.onlinejudge.core.cache.LocalQuestionCache;
import com.whut.onlinejudge.core.judge.CoreJudgeStrategy;
import com.whut.onlinejudge.core.mapper.AnswerSubmissionMapper;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 校验答案提交和相关题目 选择合适的提交处理方式
 *
 * @author liuqiao
 * @since 2024-12-01
 */
@DubboService
@AllArgsConstructor
public class AnswerSubmissionResolveServiceImpl implements AnswerSubmissionResolveService {

    private final QuestionService questionService;

    private final AnswerSubmissionMapper asMapper;

    private final CoreJudgeStrategy coreJudgeStrategy;

    private LocalQuestionCache localQuestionCache;

    @Override
    public JudgeInfo resolve(Long asId) {
        // 校验相关答案提交
        final AnswerSubmission as = asMapper.getSubmittedCodeLanguageQuestionId(asId);

        if (as == null) {
            final JudgeInfo judgeInfo = new JudgeInfo();
            judgeInfo.setMessage("相关的答案提交不存在");
            judgeInfo.setTime(0);
            judgeInfo.setMemory(0);
            return judgeInfo;
        }
        as.setId(asId);

        final CacheQuestion cacheQuestion = localQuestionCache.get(as.getQuestionId(), as.getLanguage());

        // 校验相关题目
        if (cacheQuestion.getId() == null) {
            final JudgeInfo judgeInfo = JudgeInfo.zeroLimit(RunnerStatusEnum.QUESTION_NOT_EXIST);
            updateAnswerSubmission(as, judgeInfo, SatusEnum.ERROR);
            return judgeInfo;
        }

        // 代码执行  结果处理
        final JudgeInfo judgeInfo = coreJudgeStrategy.resolve(as, cacheQuestion);
        updateAnswerSubmission(as, judgeInfo, SatusEnum.OVER);

        return judgeInfo;
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
