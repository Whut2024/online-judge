package com.whut.onlinejudge.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuqiao
 * @since 2024-11-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeCase implements Serializable {

    /**
     * 参数
     */
    private List<String> input;

    /**
     * 返回值
     */
    private List<String> output;

    private final static long serialVersionUID = -1;
}
