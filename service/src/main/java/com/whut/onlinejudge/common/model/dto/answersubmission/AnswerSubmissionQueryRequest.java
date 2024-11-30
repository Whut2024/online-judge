package com.whut.onlinejudge.common.model.dto.answersubmission;

import com.whut.onlinejudge.common.model.dto.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AnswerSubmissionQueryRequest extends PageRequest implements Serializable {

    /**
     * 创建答案提交的用户 ID
     */
    private Long userId;

    /**
     * 题目 ID
     */
    private Long questionId;

    /**
     * 提交的代码的语言
     */
    private String language;

    /**
     * 答案提交的状态 0 异常 1 通过 2 运行中
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}