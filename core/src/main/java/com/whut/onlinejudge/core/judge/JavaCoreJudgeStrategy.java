package com.whut.onlinejudge.core.judge;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.whut.onlinejudge.common.model.entity.*;
import com.whut.onlinejudge.core.cache.CacheQuestion;
import com.whut.onlinejudge.core.runner.CodeRunner;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public JudgeInfo javaResolve(AnswerSubmission as, CacheQuestion cq) {
        return runner.run(as.getLanguage(), as.getSubmittedCode(), cq, as.getId());
    }
}
