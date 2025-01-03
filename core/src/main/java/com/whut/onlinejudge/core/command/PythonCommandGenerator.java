package com.whut.onlinejudge.core.command;

/**
 * @author liuqiao
 * @since 2024-12-03
 */
public class PythonCommandGenerator extends AbstractCommandGenerator {
    @Override
    public String getExecutionCommand(String submittedCodePrefix, String coreCodePrefix) {
        return "java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s";
    }

    @Override
    public String getCompilationCoreCodeCommand(String prefix) {
        return "";
    }

    @Override
    public String getCompilationSubmittedCodeCommand(String prefix) {
        return "";
    }

    protected PythonCommandGenerator() {
    }
}
