package com.whut.onlinejudge.core.judge;

import cn.hutool.json.JSONUtil;
import com.whut.onlinejudge.common.model.entity.AnswerSubmission;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.common.model.entity.Question;
import com.whut.onlinejudge.core.runner.CodeRunner;
import com.whut.onlinejudge.core.runner.DockerCodeRunner;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * @author liuqiao
 * @since 2024-12-01
 */
@ConditionalOnBean(DockerCodeRunner.class)
@Component
@AllArgsConstructor
public class JavaCoreJudgeStrategy extends CoreJudgeStrategy {

    private final CodeRunner runner;

    @Override
    public JudgeInfo javaResolve(AnswerSubmission as, Question q) {
        return runner.run(as.getSubmittedCode(), q.getCoreCode(), JSONUtil.toBean(q.getJudgeConfig(), JudgeConfig.class));
    }
}
