package com.whut.onlinejudge.backgrounddoor.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.onlinejudge.backgrounddoor.common.BaseResponse;
import com.whut.onlinejudge.backgrounddoor.common.ResultUtils;
import com.whut.onlinejudge.common.model.dto.answersubmission.AnswerSubmissionAddRequest;
import com.whut.onlinejudge.common.model.dto.answersubmission.AnswerSubmissionQueryRequest;
import com.whut.onlinejudge.common.model.vo.AnswerSubmissionVo;
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
public class AnswerSubmissionController {

    @PostMapping("/do")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody AnswerSubmissionAddRequest answerSubmissionAddRequest) {
        return ResultUtils.success(null);
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<AnswerSubmissionVo>> listQuestionSubmitByPage(@RequestBody AnswerSubmissionQueryRequest answerSubmissionQueryRequest) {
        return ResultUtils.success(null);
    }
}
