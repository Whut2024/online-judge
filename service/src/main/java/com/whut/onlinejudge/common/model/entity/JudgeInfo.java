package com.whut.onlinejudge.common.model.entity;

import com.whut.onlinejudge.common.model.enums.RunnerStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiao
 * @since 2024-12-01
 */
@Data
@NoArgsConstructor
public class JudgeInfo implements Serializable {

    /**
     * 程序运行消耗时间
     */
    private Integer time;

    /**
     * 程序运行消耗内存
     */
    private Integer memory;

    /**
     * 程序运行的输出流
     */
    private String message;

    /**
     * 程序出现的异常
     */
    private String exception;

    public static JudgeInfo zeroLimit(RunnerStatusEnum runnerStatusEnum) {
        return new JudgeInfo(runnerStatusEnum.getName(), 0, 0);
    }

    public JudgeInfo(String exception, Integer memory, Integer time) {
        this.exception = exception;
        this.memory = memory;
        this.time = time;
    }
}
