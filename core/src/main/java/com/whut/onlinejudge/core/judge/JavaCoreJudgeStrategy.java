package com.whut.onlinejudge.core.judge;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.whut.onlinejudge.common.model.entity.*;
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
    public JudgeInfo javaResolve(AnswerSubmission as, Question q) {
        final List<String> jcStr = JSONUtil.toList(q.getJudgeCase(), String.class);
        final List<JudgeCase> judgeCaseList = new ArrayList<>(jcStr.size());
        for (String s : jcStr) {
            final JSONObject jsonObject = JSONUtil.parseObj(s);
            final JudgeCase judgeCase = new JudgeCase();
            judgeCase.setInput(JSONUtil.toBean(jsonObject.getStr("input"), new TypeReference<List<String>>() {}, false));
            judgeCase.setOutput(JSONUtil.toBean(jsonObject.getStr("output"), new TypeReference<List<String>>() {}, false));
            judgeCaseList.add(judgeCase);
        }

        return runner.run(as.getLanguage(),
                as.getSubmittedCode(),
                q.getCoreCode(),
                JSONUtil.toBean(q.getJudgeConfig(), JudgeConfig.class),
                judgeCaseList);
    }
}
