package com.whut.onlinejudge.backgrounddoor.model.dto.answersubmission;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnswerSubmissionAddRequest implements Serializable {

    /**
     * 题目 ID
     */
    private Long questionId;

    /**
     * 提交的代码
     */
    private String submittedCode;

    /**
     * 提交的代码的语言
     */
    private String language;

    private static final long serialVersionUID = 1L;
}