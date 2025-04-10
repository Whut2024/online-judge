package com.whut.onlinejudge.core.command;

/**
 * @author liuqiao
 * @since 2024-12-03
 */
public class JavaCommandGenerator extends AbstractCommandGenerator {

    private final static String MAIN_NAME = "/Main.java";

    private final static String SOLUTION_NAME = "/Solution.java";

    private final static String SOLUTION_CLASS_NAME = "/Solution.class";

    private final static String FAST_JSON_NAME = "fastjson.jar";

    @Override
    public String getExecutionCommand(String submittedCodePrefix, String coreCodePrefix) {
        final String jsonJarPath = submittedCodePrefix.substring(0, submittedCodePrefix.lastIndexOf("/target/") + 8) + FAST_JSON_NAME;

        return String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s:%s:%s Main", jsonJarPath, submittedCodePrefix, coreCodePrefix);
    }

    @Override
    public String getCompilationCoreCodeCommand(String prefix) {
        final String jsonJarPath = prefix.substring(0, prefix.lastIndexOf("/target/") + 8) + FAST_JSON_NAME;

        return String.format("javac -cp %s -d %s -encoding utf-8 %s %s",
                jsonJarPath,
                prefix,
                prefix + SOLUTION_NAME,
                prefix + MAIN_NAME);
    }

    @Override
    public String getCompilationSubmittedCodeCommand(String prefix) {
        return String.format("javac -d %s -encoding utf-8 %s",
                prefix,
                prefix + SOLUTION_NAME);
    }

    @Override
    public String getSubmittedCodeSrcPath(String prefix) {
        return prefix + SOLUTION_NAME;
    }

    @Override
    public String getCoreCodeSrcPath(String prefix) {
        return prefix + MAIN_NAME;

    }

    @Override
    public String getSubmittedDentPath(String prefix) {
        return prefix + SOLUTION_CLASS_NAME;

    }

    protected JavaCommandGenerator() {
    }
}
