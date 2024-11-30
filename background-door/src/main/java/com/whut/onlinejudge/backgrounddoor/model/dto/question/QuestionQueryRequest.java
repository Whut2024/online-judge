package com.whut.onlinejudge.backgrounddoor.model.dto.question;

import com.whut.onlinejudge.backgrounddoor.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {

    /**
     * 题目 ID
     */
    private Long id;

    /**
     * 创建题目的用户 ID
     */
    private Long userId;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目标签
     */
    private String tags;

    /**
     * 题目内容
     */
    private String content;

    private static final long serialVersionUID = 1L;
}