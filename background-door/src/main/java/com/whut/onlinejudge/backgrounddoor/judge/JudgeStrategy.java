package com.whut.onlinejudge.backgrounddoor.judge;

import com.whut.onlinejudge.common.model.entity.JudgeInfo;

/**
 * @author liuqiao
 * @since 2024-12-01
 */
public interface JudgeStrategy {

    /**
     * 实现从数据库获取相关的答案提交信息 将提交信息里面用户编写的代码配合上引导代码运行 处理运行结果 返回结果
     * @param asId 用户的答案提交 ID
     * @return 答案提交代码运行处理的结果
     */
    JudgeInfo judge(Long asId);
}
