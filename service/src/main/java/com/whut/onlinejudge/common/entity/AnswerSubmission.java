package com.whut.onlinejudge.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 答案提交表
 * @TableName t_answer_submission
 */
@TableName(value ="t_answer_submission")
@Data
public class AnswerSubmission implements Serializable {
    /**
     * 答案提交 ID
     */
    @TableId
    private Long id;

    /**
     * 创建答案提交的用户 ID
     */
    private Long userId;

    /**
     * 题目 ID
     */
    private Long questionId;

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

    /**
     * 逻辑删除 1 删除 0 未删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}