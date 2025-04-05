package com.whut.onlinejudge.core.command;

/**
 * 生成相关的编程语言运行的命令字符串
 *
 * @author liuqiao
 * @since 2024-12-03
 */
public abstract class AbstractCommandGenerator {

    /**
     * @param submittedCodePrefix 用户提交代码源文件的目录
     * @param coreCodePrefix      核心引导代码源文件的目录
     */
    public abstract String getExecutionCommand(String submittedCodePrefix, String coreCodePrefix);

    /**
     * @param prefix 源文件的目录
     */
    public abstract String getCompilationCoreCodeCommand(String prefix);

    /**
     * @param prefix 源文件的目录
     */
    public abstract String getCompilationSubmittedCodeCommand(String prefix);


    public abstract String getSubmittedCodeSrcPath(String prefix);

    public abstract String getCoreCodeSrcPath(String prefix);

    public abstract String getSubmittedDentPath(String prefix);

}
