package com.whut.onlinejudge.backgrounddoor.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class QuestionVo implements Serializable {

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

    /**
     * 题目运行资源限制
     */
    private String judgeConfig;

    /**
     * 答案提交数
     */
    private Integer submissionNumber;

    /**
     * 答案提交通过数
     */
    private Integer acceptanceNumber;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 题目创建人
     */
    private UserVo userVo;

    private static final long serialVersionUID = 1L;
}