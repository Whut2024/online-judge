package com.whut.onlinejudge.backgrounddoor.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.onlinejudge.backgrounddoor.common.BaseResponse;
import com.whut.onlinejudge.backgrounddoor.common.DeleteRequest;
import com.whut.onlinejudge.backgrounddoor.common.ResultUtils;
import com.whut.onlinejudge.backgrounddoor.model.dto.question.QuestionAddRequest;
import com.whut.onlinejudge.backgrounddoor.model.dto.question.QuestionQueryRequest;
import com.whut.onlinejudge.backgrounddoor.model.dto.question.QuestionUpdateRequest;
import com.whut.onlinejudge.backgrounddoor.model.vo.QuestionVo;
import com.whut.onlinejudge.common.entity.Question;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuqiao
 * @since 2024-11-29
 */

@RestController
@RequestMapping("/question")
public class QuestionController {


    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest) {
        return ResultUtils.success(null);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest) {
        return ResultUtils.success(null);
    }

    @GetMapping("/get")
    public BaseResponse<Question> getQuestionById(@RequestParam("id") Long id) {
        return ResultUtils.success(null);
    }


    @PostMapping("/list/page")
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        return ResultUtils.success(null);
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVo>> listQuestionVoByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        return ResultUtils.success(null);
    }


    @PostMapping("/update")
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        return ResultUtils.success(null);
    }
}
