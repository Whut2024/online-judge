package com.whut.onlinejudge.backgrounddoor.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AnswerSubmissionVo implements Serializable {
    /**
     * 答案提交 ID
     */
    private Long id;

    /**
     * 创建答案提交的用户 ID
     */
    private Long userId;

    /**
     * 创建答案提交的用户
     */
    private UserVo userVo;

    /**
     * 题目 ID
     */
    private Long questionId;

    /**
     * 答案提交关联的题目
     */
    private QuestionVo questionVo;

    /**
     * 提交的代码
     */
    private String submittedCode;

    /**
     * 答案提交的测试处理结果
     */
    private String judgeInfo;

    /**
     * 提交的代码的语言
     */
    private String language;

    /**
     * 答案提交的状态 0 异常 1 通过 2 运行中
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}