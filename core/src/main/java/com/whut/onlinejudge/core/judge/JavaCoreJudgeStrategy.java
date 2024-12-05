package com.whut.onlinejudge.core.judge;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.whut.onlinejudge.common.model.entity.*;
import com.whut.onlinejudge.core.runner.CodeRunner;
import com.whut.onlinejudge.core.runner.docker.DockerCodeRunner;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liuqiao
 * @since 2024-12-01
 */
@Component
@AllArgsConstructor
public class JavaCoreJudgeStrategy extends CoreJudgeStrategy {

    private final CodeRunner runner;

    @Override
    public JudgeInfo javaResolve(AnswerSubmission as, Question q) {
        return runner.run(as.getLanguage(),
                as.getSubmittedCode(),
                q.getCoreCode(),
                JSONUtil.toBean(q.getJudgeConfig(), JudgeConfig.class), JSONUtil.toBean(q.getJudgeCase(), new TypeReference<List<JudgeCase>>() {
                }, false));
    }
}
