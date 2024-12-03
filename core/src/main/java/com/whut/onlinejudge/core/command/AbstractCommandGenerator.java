package com.whut.onlinejudge.core.command;

/**
 * 生成相关的编程语言运行的命令字符串
 * @author liuqiao
 * @since 2024-12-03
 */
public abstract class AbstractCommandGenerator {

    /**
     * @param args 这个参数留给以后扩展
     */
    public abstract String getExecutionCommand(Object args);

    /**
     * @param args 这个参数留给以后扩展
     */
    public abstract String getCompilationCommand(Object args);
}
