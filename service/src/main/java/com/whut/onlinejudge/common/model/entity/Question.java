package com.whut.onlinejudge.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 题目表
 * @TableName t_question
 */
@TableName(value ="t_question")
@Data
public class Question implements Serializable {
    /**
     * 题目 ID
     */
    @TableId
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
     * 运行测试案例
     */
    private String judgeCase;

    /**
     * 运行校验代码
     */
    private String coreCode;

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
     * 逻辑删除 1 删除 0 未删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}