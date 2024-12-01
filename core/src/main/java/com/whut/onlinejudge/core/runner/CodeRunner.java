package com.whut.onlinejudge.core.runner;

import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;

/**
 * 运行引导代码和用户提交的代码的类
 * @author liuqiao
 * @since 2024-12-01
 */
public interface CodeRunner {

    /**
     * @param submittedCode 用户提交的代码
     * @param coreCode 引导代码
     * @param judgeConfig 运行现状条件
     * @return 运行过程中的异常和资源消耗
     */
    JudgeInfo run(String submittedCode, String coreCode, JudgeConfig judgeConfig);
}
