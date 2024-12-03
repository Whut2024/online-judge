package com.whut.onlinejudge.core.command;

/**
 * @author liuqiao
 * @since 2024-12-03
 */
public class GoCommandGenerator extends AbstractCommandGenerator {
    @Override
    public String getExecutionCommand(Object args) {
        return "java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s";
    }

    @Override
    public String getCompilationCommand(Object args) {
        return "";
    }

    protected GoCommandGenerator() {
    }
}
