package com.whut.onlinejudge.common.service;

import com.whut.onlinejudge.common.model.entity.JudgeInfo;

/**
 * RPC 远程调用处理答案提交接口
 * @author liuqiao
 * @since 2024-12-01
 */
public interface AnswerSubmissionResolveService {


    JudgeInfo resolve(Long asId);
}
