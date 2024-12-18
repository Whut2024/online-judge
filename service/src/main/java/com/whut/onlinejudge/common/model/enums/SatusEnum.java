package com.whut.onlinejudge.common.model.enums;


import lombok.Getter;

/**
 * 答案提交处理状态枚举
 */
@Getter
public enum SatusEnum {

    ERROR("异常", 0),
    OVER("完成", 1),
    RUNNING("运行中", 2);

    private final String name;

    private final Integer value;

    SatusEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static boolean exists(Integer value) {
        for (SatusEnum satusEnum : values()) {
            if (satusEnum.value.equals(value))
                return true;
        }
        return false;
    }

}
