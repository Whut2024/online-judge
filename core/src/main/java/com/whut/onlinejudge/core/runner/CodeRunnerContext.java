package com.whut.onlinejudge.core.runner;

import lombok.Data;

/**
 * 代码执行时的上下文
 *
 * @author liuqiao
 * @since 2024-12-02
 */
@Data
public class CodeRunnerContext {

    /**
     * 错误栈帧
     */
    private String exception;

    /**
     * 是否通过测试
     */
    private boolean pass;

    /**
     * 代码运行过程中的输出
     */
    private String output;

    /**
     * 内存限制
     */
    private Integer memoryLimit;

    /**
     * 时间限制
     */
    private Integer timeLimit;

}
