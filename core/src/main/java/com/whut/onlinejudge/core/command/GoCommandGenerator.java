package com.whut.onlinejudge.core.command;

import java.io.File;

/**
 * @author liuqiao
 * @since 2024-12-03
 */
public class GoCommandGenerator extends AbstractCommandGenerator {

    private final String MAIN_NAME = "Main.go";

    private final String SOLUTION_NAME = "solution.go";

    @Override
    public String getExecutionCommand(String submittedCodePrefix, String coreCodePrefix) {
        final String coreCodePath = coreCodePrefix + File.separator + MAIN_NAME;
        return String.format("cp %s %s && go run %s%s%s %s%s%s",
                coreCodePath, submittedCodePrefix,
                submittedCodePrefix, File.separator, MAIN_NAME,
                submittedCodePrefix, File.separator, SOLUTION_NAME);

        /*return String.format("go run %s%s%s %s%s%s",*/
        /*        submittedCodePrefix, File.separator, MAIN_NAME,*/
        /*        submittedCodePrefix, File.separator, SOLUTION_NAME);*/
    }

    @Override
    public String getCompilationCoreCodeCommand(String prefix) {
        return NO_COMPILATION_COMMAND;
    }

    @Override
    public String getCompilationSubmittedCodeCommand(String prefix) {
        return NO_COMPILATION_COMMAND;
    }

    @Override
    public String getSubmittedCodeSrcPath(String prefix) {
        return prefix + File.separator + SOLUTION_NAME;
    }

    @Override
    public String getCoreCodeSrcPath(String prefix) {
        return prefix + File.separator + MAIN_NAME;
    }

    @Override
    public String getSubmittedDentPath(String prefix) {
        return NO_COMPILED_PATH;
    }

    protected GoCommandGenerator() {
    }
}
