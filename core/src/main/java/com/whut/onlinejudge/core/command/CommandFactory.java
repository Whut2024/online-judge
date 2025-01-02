package com.whut.onlinejudge.core.command;

import com.whut.onlinejudge.common.model.enums.LanguageEnum;
import com.whut.onlinejudge.core.constant.JavaCodeConstant;

import java.util.HashMap;

/**
 * 集中管理根据编程语言生成相关执行命令字符串的工厂
 *
 * @author liuqiao
 * @since 2024-12-03
 */
public class CommandFactory {
    private final static HashMap<String, AbstractCommandGenerator> generatorMap;

    static {
        generatorMap = new HashMap<>(16);
        generatorMap.put(LanguageEnum.JAVA.getName(), new JavaCommandGenerator());
        generatorMap.put(LanguageEnum.C.getName(), new CCommandGenerator());
        generatorMap.put(LanguageEnum.C_PLUS.getName(), new CppCommandGenerator());
        generatorMap.put(LanguageEnum.GO.getName(), new GoCommandGenerator());
        generatorMap.put(LanguageEnum.PYTHON.getName(), new PythonCommandGenerator());
    }

    public static String getExecutionCommand(String language, String prefix) {
        final AbstractCommandGenerator generator;
        return (generator = generatorMap.get(language)) != null ? generator.getExecutionCommand(prefix) : null;
    }

    public static String getCompilationCommand(String language, String prefix) {
        final AbstractCommandGenerator generator;
        return (generator = generatorMap.get(language)) != null ? generator.getCompilationCommand(prefix) : null;
    }

    /**
     * 以后扩展可以修改成根据语言拼接
     */
    public static String getSubmittedSrc(String language, String prefix) {
        return prefix + JavaCodeConstant.SOLUTION_NAME;
    }

    public static String getCoreCodeSrc(String language, String prefix) {
        return prefix + JavaCodeConstant.MAIN_NAME;
    }
}
