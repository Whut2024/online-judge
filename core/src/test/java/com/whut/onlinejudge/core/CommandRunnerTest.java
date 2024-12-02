package com.whut.onlinejudge.core;

import org.mockito.internal.util.io.IOUtil;

import java.util.Collection;

/**
 * @author liuqiao
 * @since 2024-12-02
 */
public class CommandRunnerTest {
    public static void main(String[] args) throws Throwable {
        final Runtime runtime = Runtime.getRuntime();
        final Process process = runtime.exec("echo lff");
        final int state = process.waitFor();
        if (state == 0) {
            Collection<String> collection = IOUtil.readLines(process.getInputStream());
            collection.forEach(System.out::println);
        }

    }
}
