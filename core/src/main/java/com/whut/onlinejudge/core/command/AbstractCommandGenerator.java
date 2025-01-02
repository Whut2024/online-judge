package com.whut.onlinejudge.core.command;

/**
 * 生成相关的编程语言运行的命令字符串
 * @author liuqiao
 * @since 2024-12-03
 */
public abstract class AbstractCommandGenerator {

    /**
     * @param prefix 源文件的目录
     */
    public abstract String getExecutionCommand(String prefix);

    /**
     * @param prefix 源文件的目录
     */
    public abstract String getCompilationCommand(String prefix);
}
