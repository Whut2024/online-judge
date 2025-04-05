package com.whut.onlinejudge.core.command;

import com.whut.onlinejudge.common.model.enums.LanguageEnum;

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
        //generatorMap.put(LanguageEnum.C.getName(), new CCommandGenerator());
        //generatorMap.put(LanguageEnum.C_PLUS.getName(), new CppCommandGenerator());
        generatorMap.put(LanguageEnum.GO.getName(), new GoCommandGenerator());
        //generatorMap.put(LanguageEnum.PYTHON.getName(), new PythonCommandGenerator());
    }

    public static String getExecutionCommand(String language, String submittedCodePrefix, String coreCodeCodePrefix) {
        final AbstractCommandGenerator generator;
        return (generator = generatorMap.get(language)) != null ?
                generator.getExecutionCommand(submittedCodePrefix, coreCodeCodePrefix) : null;
    }

    public static String getCompilationCoreCodeCommand(String language, String prefix) {
        final AbstractCommandGenerator generator;
        return (generator = generatorMap.get(language)) != null ?
                generator.getCompilationCoreCodeCommand(prefix) : null;
    }

    public static String getCompilationSubmittedCodeCommand(String language, String prefix) {
        final AbstractCommandGenerator generator;
        return (generator = generatorMap.get(language)) != null ?
                generator.getCompilationSubmittedCodeCommand(prefix) : null;
    }

    /**
     * 以后扩展可以修改成根据语言拼接
     */
    public static String getSubmittedSrc(String language, String prefix) {
        final AbstractCommandGenerator generator;
        return (generator = generatorMap.get(language)) != null ?
                generator.getSubmittedCodeSrcPath(prefix) : null;
    }

    public static String getCoreCodeSrc(String language, String prefix) {
        final AbstractCommandGenerator generator;
        return (generator = generatorMap.get(language)) != null ?
                generator.getCoreCodeSrcPath(prefix) : null;
    }

    public static String getSubmittedDent(String language, String prefix) {
        final AbstractCommandGenerator generator;
        return (generator = generatorMap.get(language)) != null ?
                generator.getSubmittedDentPath(prefix) : null;
    }
}
