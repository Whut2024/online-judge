package com.whut.onlinejudge.common.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liuqiao
 * @since 2024-12-03
 */
@AllArgsConstructor
@Getter
public enum RunnerStatusEnum {

    PASS("答案通过"),
    COMPILE_FAIL("编译失败"),
    TIME_OUT("超时"),
    MEMORY_FAIL("内存溢出"),
    LANGUAGE_ERROR("编程语言错误"),
    QUESTION_NOT_EXIST("相关题目不存在"),
    INNER("内部错误");

    private final String name;

}
