package com.whut.onlinejudge.backgrounddoor.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whut.onlinejudge.backgrounddoor.common.BaseResponse;
import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import com.whut.onlinejudge.backgrounddoor.constant.JudgeConstant;
import com.whut.onlinejudge.backgrounddoor.constant.MysqlConstant;
import com.whut.onlinejudge.backgrounddoor.exception.ThrowUtils;
import com.whut.onlinejudge.common.model.dto.DeleteRequest;
import com.whut.onlinejudge.backgrounddoor.common.ResultUtils;
import com.whut.onlinejudge.common.model.dto.question.QuestionAddRequest;
import com.whut.onlinejudge.common.model.dto.question.QuestionQueryRequest;
import com.whut.onlinejudge.common.model.dto.question.QuestionUpdateRequest;
import com.whut.onlinejudge.common.model.entity.JudgeCase;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.vo.QuestionVo;
import com.whut.onlinejudge.common.model.entity.Question;
import com.whut.onlinejudge.common.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liuqiao
 * @since 2024-11-29
 */

@RestController
@RequestMapping("/question")
@AllArgsConstructor
public class QuestionController {


    private final QuestionService questionService;

    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest) {
        ThrowUtils.throwIf(questionAddRequest == null,
                ErrorCode.PARAMS_ERROR, "请求体为NULL");
        final String title = questionAddRequest.getTitle();
        final List<String> tags = questionAddRequest.getTags();
        final String content = questionAddRequest.getContent();
        final List<JudgeCase> judgeCaseList = questionAddRequest.getJudgeCase();
        final JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        final String coreCode = questionAddRequest.getCoreCode();

        ThrowUtils.throwIf(!StrUtil.isAllNotBlank(title, content, coreCode),
                ErrorCode.PARAMS_ERROR, "参数为NULL");
        ThrowUtils.throwIf(title.length() > 32 ||
                        content.length() > MysqlConstant.TEXT_MAX_LENGTH ||
                        coreCode.length() > MysqlConstant.TEXT_MAX_LENGTH,
                ErrorCode.PARAMS_ERROR, "标题或者内容过长");
        if (CollectionUtil.isNotEmpty(tags)) {
            int tagTotalLength = 0;
            for (String tag : tags) {
                if (tag != null)
                    tagTotalLength += tag.length();
            }
            ThrowUtils.throwIf(tagTotalLength > 128,
                    ErrorCode.PARAMS_ERROR, "标签过长");
        }

        ThrowUtils.throwIf(CollectionUtil.isEmpty(judgeCaseList) ||
                        judgeConfig == null,
                ErrorCode.PARAMS_ERROR, "测试案例或者测试条件为NULL");
        int jcTotalLength = 0;
        String x;
        for (JudgeCase judgeCase : judgeCaseList) {
            x = judgeCase.getInput();
            if (x != null)
                jcTotalLength += x.length();
            x = judgeCase.getOutput();
            if (x != null)
                jcTotalLength += x.length();
        }
        ThrowUtils.throwIf(jcTotalLength > MysqlConstant.TEXT_MAX_LENGTH,
                ErrorCode.PARAMS_ERROR, "测试案例过长");

        final Integer memoryLimit = judgeConfig.getMemoryLimit();
        final Integer timeLimit = judgeConfig.getTimeLimit();
        ThrowUtils.throwIf(memoryLimit == null || timeLimit == null,
                ErrorCode.PARAMS_ERROR, "内存限制或者时间限制为NULL");
        ThrowUtils.throwIf(memoryLimit > JudgeConstant.MEMORY_MAX_LIMIT ||
                        timeLimit > JudgeConstant.TIME_MAX_LIMIT,
                ErrorCode.PARAMS_ERROR, "内存限制或者时间限制过大");


        return ResultUtils.success(questionService.addQuestion(questionAddRequest));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestion(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null,
                ErrorCode.PARAMS_ERROR, "请求体为NULL");
        final Long id = deleteRequest.getId();
        ThrowUtils.throwIf(id == null ||
                        id < 0,
                ErrorCode.PARAMS_ERROR, "ID 错误");

        return ResultUtils.success(questionService.deleteQuestion(id));
    }

    @GetMapping("/get")
    public BaseResponse<Question> getQuestionById(@RequestParam("id") Long id) {
        ThrowUtils.throwIf(id == null ||
                        id < 0,
                ErrorCode.PARAMS_ERROR, "ID 错误");

        return ResultUtils.success(questionService.getQuestionById(id));
    }


    @PostMapping("/list/page")
    public BaseResponse<Page<Question>> listQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        ThrowUtils.throwIf(questionQueryRequest == null,
                ErrorCode.PARAMS_ERROR, "请求体为NULL");
        final Long id = questionQueryRequest.getId();
        final Long userId = questionQueryRequest.getUserId();
        final String title = questionQueryRequest.getTitle();
        final List<String> tags = questionQueryRequest.getTags();
        final String content = questionQueryRequest.getContent();
        final Long endId = questionQueryRequest.getEndId();

        ThrowUtils.throwIf(id != null && id < 0,
                ErrorCode.PARAMS_ERROR, "ID 错误");
        ThrowUtils.throwIf(userId != null && userId < 0,
                ErrorCode.PARAMS_ERROR, "用户 ID 错误");
        ThrowUtils.throwIf(endId != null && endId < 0,
                ErrorCode.PARAMS_ERROR, "ID 错误");
        ThrowUtils.throwIf(title != null && title.length() > 32,
                ErrorCode.PARAMS_ERROR, "标题过长");
        ThrowUtils.throwIf(content != null && content.length() > MysqlConstant.TEXT_MAX_LENGTH,
                ErrorCode.PARAMS_ERROR, "内容过长");
        if (CollectionUtil.isNotEmpty(tags)) {
            int tagTotalLength = 0;
            for (String tag : tags) {
                if (tag != null)
                    tagTotalLength += tag.length();
            }
            ThrowUtils.throwIf(tagTotalLength > 128,
                    ErrorCode.PARAMS_ERROR, "标签过长");
        }

        return ResultUtils.success(questionService.listQuestionByPage(questionQueryRequest));
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionVo>> listQuestionVoByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        ThrowUtils.throwIf(questionQueryRequest == null,
                ErrorCode.PARAMS_ERROR, "请求体为NULL");
        final Long id = questionQueryRequest.getId();
        final Long userId = questionQueryRequest.getUserId();
        final String title = questionQueryRequest.getTitle();
        final List<String> tags = questionQueryRequest.getTags();
        final String content = questionQueryRequest.getContent();
        final Long endId = questionQueryRequest.getEndId();

        ThrowUtils.throwIf(id != null && id < 0,
                ErrorCode.PARAMS_ERROR, "ID 错误");
        ThrowUtils.throwIf(userId != null && userId < 0,
                ErrorCode.PARAMS_ERROR, "用户 ID 错误");
        ThrowUtils.throwIf(endId != null && endId < 0,
                ErrorCode.PARAMS_ERROR, "ID 错误");
        ThrowUtils.throwIf(title != null && title.length() > 32,
                ErrorCode.PARAMS_ERROR, "标题过长");
        ThrowUtils.throwIf(content != null && content.length() > MysqlConstant.TEXT_MAX_LENGTH,
                ErrorCode.PARAMS_ERROR, "内容过长");
        if (CollectionUtil.isNotEmpty(tags)) {
            int tagTotalLength = 0;
            for (String tag : tags) {
                if (tag != null)
                    tagTotalLength += tag.length();
            }
            ThrowUtils.throwIf(tagTotalLength > 128,
                    ErrorCode.PARAMS_ERROR, "标签过长");
        }

        return ResultUtils.success(questionService.listQuestionVoByPage(questionQueryRequest));
    }


    @PostMapping("/update")
    public BaseResponse<Boolean> updateQuestion(@RequestBody QuestionUpdateRequest questionUpdateRequest) {
        ThrowUtils.throwIf(questionUpdateRequest == null,
                ErrorCode.PARAMS_ERROR, "请求体为NULL");

        final Long id = questionUpdateRequest.getId();
        final String title = questionUpdateRequest.getTitle();
        final List<String> tags = questionUpdateRequest.getTags();
        final List<JudgeCase> judgeCaseList = questionUpdateRequest.getJudgeCase();
        final JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
        final String content = questionUpdateRequest.getContent();
        final String coreCode = questionUpdateRequest.getCoreCode();

        ThrowUtils.throwIf(id == null || id < 0,
                ErrorCode.PARAMS_ERROR, "ID 错误");
        ThrowUtils.throwIf(title != null && title.length() > 32,
                ErrorCode.PARAMS_ERROR, "标题过长");
        ThrowUtils.throwIf(content != null && content.length() > MysqlConstant.TEXT_MAX_LENGTH,
                ErrorCode.PARAMS_ERROR, "内容过长");
        ThrowUtils.throwIf(coreCode != null && coreCode.length() > MysqlConstant.TEXT_MAX_LENGTH,
                ErrorCode.PARAMS_ERROR, "引导代码过长");

        if (CollectionUtil.isNotEmpty(tags)) {
            int tagTotalLength = 0;
            for (String tag : tags) {
                if (tag != null)
                    tagTotalLength += tag.length();
            }
            ThrowUtils.throwIf(tagTotalLength > 128,
                    ErrorCode.PARAMS_ERROR, "标签过长");
        }

        if (CollectionUtil.isNotEmpty(judgeCaseList)) {
            int jcTotalLength = 0;
            String x;
            for (JudgeCase judgeCase : judgeCaseList) {
                x = judgeCase.getInput();
                if (x != null)
                    jcTotalLength += x.length();
                x = judgeCase.getOutput();
                if (x != null)
                    jcTotalLength += x.length();
            }
            ThrowUtils.throwIf(jcTotalLength > MysqlConstant.TEXT_MAX_LENGTH,
                    ErrorCode.PARAMS_ERROR, "测试案例过长");
        }

        if (judgeConfig != null) {
            final Integer memoryLimit = judgeConfig.getMemoryLimit();
            final Integer timeLimit = judgeConfig.getTimeLimit();
            ThrowUtils.throwIf(memoryLimit == null || timeLimit == null,
                    ErrorCode.PARAMS_ERROR, "内存限制或者时间限制为NULL");
            ThrowUtils.throwIf(memoryLimit > JudgeConstant.MEMORY_MAX_LIMIT ||
                            timeLimit > JudgeConstant.TIME_MAX_LIMIT,
                    ErrorCode.PARAMS_ERROR, "内存限制或者时间限制过大");
        }

        return ResultUtils.success(questionService.updateQuestion(questionUpdateRequest));
    }
}
