package com.whut.onlinejudge.backgrounddoor.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.onlinejudge.backgrounddoor.common.BaseResponse;
import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import com.whut.onlinejudge.backgrounddoor.common.ResultUtils;
import com.whut.onlinejudge.backgrounddoor.constant.MysqlConstant;
import com.whut.onlinejudge.backgrounddoor.exception.ThrowUtils;
import com.whut.onlinejudge.common.model.dto.answersubmission.AnswerSubmissionAddRequest;
import com.whut.onlinejudge.common.model.dto.answersubmission.AnswerSubmissionQueryRequest;
import com.whut.onlinejudge.common.model.enums.LanguageEnum;
import com.whut.onlinejudge.common.model.enums.SatusEnum;
import com.whut.onlinejudge.common.model.vo.AnswerSubmissionVo;
import com.whut.onlinejudge.common.service.AnswerSubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuqiao
 * @since 2024-11-29
 */

@RestController
@RequestMapping("/answer_submission")
@AllArgsConstructor
public class AnswerSubmissionController {

    private final AnswerSubmissionService answerSubmissionService;

    @PostMapping("/do")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody AnswerSubmissionAddRequest answerSubmissionAddRequest) {
        ThrowUtils.throwIf(answerSubmissionAddRequest == null,
                ErrorCode.PARAMS_ERROR, "请求体为NULL");
        final Long questionId = answerSubmissionAddRequest.getQuestionId();
        final String submittedCode = answerSubmissionAddRequest.getSubmittedCode();
        final String language = answerSubmissionAddRequest.getLanguage();

        ThrowUtils.throwIf(questionId == null || questionId <= 0 || !StrUtil.isAllNotBlank(submittedCode, language),
                ErrorCode.PARAMS_ERROR, "参数为NULL");

        ThrowUtils.throwIf(submittedCode.length() > MysqlConstant.TEXT_MAX_LENGTH ||
                        language.length() > 8 || !LanguageEnum.exists(language),
                ErrorCode.PARAMS_ERROR, "代码或者编程语言选择错误");

        return ResultUtils.success(answerSubmissionService.doQuestionSubmit(answerSubmissionAddRequest));
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<AnswerSubmissionVo>> listQuestionSubmitByPage(@RequestBody AnswerSubmissionQueryRequest answerSubmissionQueryRequest) {
        ThrowUtils.throwIf(answerSubmissionQueryRequest == null,
                ErrorCode.PARAMS_ERROR, "请求体为NULL");
        final Long userId = answerSubmissionQueryRequest.getUserId();
        final Long questionId = answerSubmissionQueryRequest.getQuestionId();
        final String language = answerSubmissionQueryRequest.getLanguage();
        final Integer status = answerSubmissionQueryRequest.getStatus();

        ThrowUtils.throwIf(questionId != null && questionId <= 0,
                ErrorCode.PARAMS_ERROR, "题目 ID 错误");
        ThrowUtils.throwIf(userId != null && userId <= 0,
                ErrorCode.PARAMS_ERROR, "用户 ID 错误");

        ThrowUtils.throwIf((StrUtil.isNotBlank(language) && !LanguageEnum.exists(language)) ||
                        (status != null && !SatusEnum.exists(status)),
                ErrorCode.PARAMS_ERROR, "编程语言或者答案提交状态错误");

        return ResultUtils.success(answerSubmissionService.listQuestionSubmitByPage(answerSubmissionQueryRequest));
    }
}
