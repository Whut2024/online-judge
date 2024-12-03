package com.whut.onlinejudge.core.command;

/**
 * @author liuqiao
 * @since 2024-12-03
 */
public class CCommandGenerator extends AbstractCommandGenerator {
    @Override
    public String get(Object args) {
        return "java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s";
    }

    protected CCommandGenerator() {
    }
}
