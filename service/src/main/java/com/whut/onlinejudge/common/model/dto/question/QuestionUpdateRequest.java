package com.whut.onlinejudge.common.model.dto.question;

import com.whut.onlinejudge.common.model.entity.JudgeCase;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionUpdateRequest implements Serializable {

    /**
     * 题目 ID
     */
    private Long id;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目标签
     */
    private List<String> tags;

    /**
     * 运行测试案例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 题目运行资源限制
     */
    private JudgeConfig judgeConfig;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 程序启动引导代码
     */
    private String coreCode;

    private static final long serialVersionUID = 1L;
}