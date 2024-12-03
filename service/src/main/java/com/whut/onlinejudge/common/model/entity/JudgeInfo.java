package com.whut.onlinejudge.common.model.entity;

import com.whut.onlinejudge.common.model.enums.RunnerStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiao
 * @since 2024-12-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
     * 程序运行而外信息
     */
    private String message;

    public static JudgeInfo zeroLimit(RunnerStatusEnum runnerStatusEnum) {
        return new JudgeInfo(0, 0, runnerStatusEnum.getName());
    }
}
