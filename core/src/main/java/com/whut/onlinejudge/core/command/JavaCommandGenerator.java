package com.whut.onlinejudge.core.command;

import com.whut.onlinejudge.core.constant.JavaCodeConstant;

/**
 * @author liuqiao
 * @since 2024-12-03
 */
public class JavaCommandGenerator extends AbstractCommandGenerator {
    @Override
    public String getExecutionCommand(String submittedCodePrefix, String coreCodePrefix) {
        final String jsonJarPath = submittedCodePrefix.substring(0, submittedCodePrefix.lastIndexOf("/target/") + 8) + JavaCodeConstant.FAST_JSON_NAME;

        return String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s:%s:%s Main", jsonJarPath, submittedCodePrefix, coreCodePrefix);
    }

    @Override
    public String getCompilationCoreCodeCommand(String prefix) {
        final String jsonJarPath = prefix.substring(0, prefix.lastIndexOf("/target/") + 8) + JavaCodeConstant.FAST_JSON_NAME;

        return String.format("javac -cp %s -d %s -encoding utf-8 %s %s",
                jsonJarPath,
                prefix,
                prefix + JavaCodeConstant.SOLUTION_NAME,
                prefix + JavaCodeConstant.MAIN_NAME);
    }

    @Override
    public String getCompilationSubmittedCodeCommand(String prefix) {
        return String.format("javac -d %s -encoding utf-8 %s",
                prefix,
                prefix + JavaCodeConstant.SOLUTION_NAME);
    }

    protected JavaCommandGenerator() {
    }
}
