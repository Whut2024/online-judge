package com.whut.onlinejudge.core.command;

import com.whut.onlinejudge.core.constant.JavaCodeConstant;

import java.io.File;

/**
 * @author liuqiao
 * @since 2024-12-03
 */
public class JavaCommandGenerator extends AbstractCommandGenerator {
    @Override
    public String getExecutionCommand(String prefix) {
        final String jsonJarPath = prefix.substring(0, prefix.lastIndexOf(File.separator) + 1) + JavaCodeConstant.FAST_JSON_NAME;

        return String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s:%s Main", jsonJarPath, prefix);
    }

    @Override
    public String getCompilationCommand(String prefix) {
        final String jsonJarPath = prefix.substring(0, prefix.lastIndexOf(File.separator) + 1) + JavaCodeConstant.FAST_JSON_NAME;

        return String.format("javac -cp %s -d %s -encoding utf-8 %s %s",
                jsonJarPath,
                prefix,
                prefix + JavaCodeConstant.SOLUTION_NAME,
                prefix + JavaCodeConstant.MAIN_NAME);
    }

    protected JavaCommandGenerator() {
    }
}
