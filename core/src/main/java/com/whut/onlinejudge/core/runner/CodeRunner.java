package com.whut.onlinejudge.core.runner;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.whut.onlinejudge.common.model.entity.JudgeCase;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.core.constant.CodeConstant;

import java.util.List;

/**
 * 运行引导代码和用户提交的代码的类
 *
 * @author liuqiao
 * @since 2024-12-01
 */
public abstract class CodeRunner {

    /**
     * @param submittedCode 用户提交的代码
     * @param coreCode      引导代码
     * @param judgeConfig   运行现状条件
     * @param judgeCaseList 代码输入和输出
     * @return 运行过程中的异常和资源消耗
     */
    public abstract JudgeInfo run(String submittedCode, String coreCode, JudgeConfig judgeConfig, List<JudgeCase> judgeCaseList);

    /**
     * 处理代码结果返回
     */
    protected JudgeInfo extractContext(CodeRunnerContext runnerContext) {
        final JudgeInfo judgeInfo = new JudgeInfo();
        if (StrUtil.isBlank(runnerContext.getException())) {
            // 正常运行
            judgeInfo.setMemory(runnerContext.getMemoryLimit());
            judgeInfo.setTime(runnerContext.getTimeLimit());
            judgeInfo.setMessage(runnerContext.getOutput());
        } else {
            judgeInfo.setTime(0);
            judgeInfo.setMemory(0);
            judgeInfo.setMessage(runnerContext.getOutput() + "\n" + runnerContext.getException());
        }

        return judgeInfo;
    }

    /**
     * @return 内存 时间 测试案例个数 测试案例
     */
    protected String getInputArgs(JudgeConfig judgeConfig, List<JudgeCase> judgeCaseList) {
        StringBuilder args = new StringBuilder();
        args.append(judgeConfig.getMemoryLimit()); // 内存
        args.append(" ");
        args.append(judgeConfig.getTimeLimit()); // 时间
        if (CollectionUtil.isNotEmpty(judgeCaseList)) {
            args.append(" ");
            args.append(judgeCaseList.size()); // 个数
            // 测试案例
            if (StrUtil.isNotBlank(judgeCaseList.get(0).getOutput())) {
                for (JudgeCase judgeCase : judgeCaseList) {
                    args.append(" ");
                    args.append(judgeCase.getInput());
                    args.append(" ");
                    args.append(judgeCase.getOutput());
                }
            } else {
                for (JudgeCase judgeCase : judgeCaseList) {
                    args.append(" ");
                    args.append(judgeCase.getInput()); // 个数
                }
            }
        } else {
            args.append(" ");
            args.append(0);
        }

        return args.toString();
    }

    /**
     * @param outputList // 输出-没有异常-未通过 // 输出-异常-有异常-未通过 // 输出-内存-时间-通过
     */
    protected void extractOutput(List<String> outputList, CodeRunnerContext runnerContext) {
        final int size = outputList.size();
        final boolean pass = CodeConstant.TRUE.equals(outputList.get(size - 1));
        int outputSize = -1;
        if (pass) {
            // 通过
            runnerContext.setPass(true);
            runnerContext.setMemoryLimit(Integer.parseInt(outputList.get(size - 3)));
            runnerContext.setTimeLimit(Integer.parseInt(outputList.get(size - 2)));
            outputSize = size - 3;
        } else {
            // 没有通过
            if (CodeConstant.TRUE.equals(outputList.get(size - 2))) {
                // 异常
                runnerContext.setException(outputList.get(size - 3));
                outputSize = size - 3;
            } else
                // 没有异常
                outputSize = size - 2;

        }
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < outputSize; i++) {
            builder.append(outputList.get(i));
        }
        runnerContext.setOutput(builder.toString());
    }
}
