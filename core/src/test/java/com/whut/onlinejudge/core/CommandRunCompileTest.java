package com.whut.onlinejudge.core;

import cn.hutool.core.io.IoUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author liuqiao
 * @since 2024-12-02
 */
public class CommandRunCompileTest {
    public static void main(String[] args) throws Throwable {
        final String shellPath = "/Users/laowang/developer/codes/online-judge/core/src/test/compile-java.sh";
        final String sourceCode = "public class Solution {\n" +
                "    public Solution() {\n" +
                "    }\n" +
                "\n" +
                "    public void test() {\n" +
                "        System.out.println(\"pass\");\n" +
                "    }\n" +
                "}\n";

        final String sourceCodePath = "/Users/laowang/developer/codes/online-judge/core/src/test/Main.java",
                compiledCodePath = "/Users/laowang/developer/codes/online-judge/core/src/test";
        final Runtime runtime = Runtime.getRuntime();
        final Process process = runtime.exec(String.format("%s '$(cat << EOF %s EOF)' %s %s",
                shellPath, sourceCode, sourceCodePath, compiledCodePath));
        final int state = process.waitFor();
        if (state == 0)
            System.out.println("OK");
        else {
            System.out.println("Fail");
            System.out.println(IoUtil.read(process.getErrorStream(), StandardCharsets.UTF_8));
        }

        System.out.println(IoUtil.read(process.getInputStream(), StandardCharsets.UTF_8));

    }
}
