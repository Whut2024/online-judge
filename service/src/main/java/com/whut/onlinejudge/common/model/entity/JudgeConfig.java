package com.whut.onlinejudge.common.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiao
 * @since 2024-11-30
 */
@Data
public class JudgeConfig implements Serializable {

    /**
     * 内存限制
     */
    private Integer memoryLimit;

    /**
     * 时间限制
     */
    private Integer timeLimit;

    private final static long serialVersionUID = -1;
}
