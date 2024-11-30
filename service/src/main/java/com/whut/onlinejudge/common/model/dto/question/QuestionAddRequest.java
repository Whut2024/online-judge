package com.whut.onlinejudge.common.model.dto.question;

import com.whut.onlinejudge.common.model.entity.JudgeCase;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionAddRequest implements Serializable {

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目标签
     */
    private List<String> tags;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 运行测试案例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 题目运行资源限制
     */
    private JudgeConfig judgeConfig;

    private static final long serialVersionUID = 1L;
}