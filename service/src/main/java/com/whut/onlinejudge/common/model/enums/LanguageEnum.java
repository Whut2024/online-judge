package com.whut.onlinejudge.common.model.enums;


import lombok.Getter;

/**
 * 编程语言枚举
 */
@Getter
public enum LanguageEnum {

    JAVA("JAVA"),
    C("C"),
    C_PLUS("C_PLUS"),
    PYTHON("PYTHON"),
    GO("GO");

    private final String name;


    LanguageEnum(String name) {
        this.name = name;
    }

    public static boolean exists(String name) {
        for (LanguageEnum languageEnum : values()) {
            if (languageEnum.name.equals(name))
                return true;
        }
        return false;
    }

    public static LanguageEnum getEnum(String name) {
        for (LanguageEnum languageEnum : values()) {
            if (languageEnum.name.equals(name))
                return languageEnum;
        }

        return null;
    }

}
