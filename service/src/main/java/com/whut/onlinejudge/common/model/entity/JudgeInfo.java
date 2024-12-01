package com.whut.onlinejudge.common.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiao
 * @since 2024-12-01
 */
@Data
public class JudgeInfo implements Serializable {

    /**
     * 程序运行消耗时间
     */
    private Integer time;

    /**
     * 程序运行消耗内存
     */
    private Integer memory;

    /**
     * 程序运行而外信息
     */
    private String message;
}
