package com.whut.onlinejudge.backgrounddoor;

/**
 * @author liuqiao
 * @since 2024-12-04
 */
public class CodeFormatTest {
    public static void main(String[] args) {
        final String code = "public class Solution {\n" +
                "\n" +
                "    public int add(int a, int b) {\n" +
                "        System.out.println(\"a = \" + a);\n" +
                "        System.out.println(\"b = \" + b);\n" +
                "\n" +
                "        return a + b;\n" +
                "    }\n" +
                "}";
        final String[] data = code.split("\n");
        final StringBuilder builder = new StringBuilder();
        for (String s : data) {
            builder.append(s).append(" ");
        }


        System.out.println(builder);
    }
}
