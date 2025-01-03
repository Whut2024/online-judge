package com.whut.onlinejudge.core.judge;

import com.whut.onlinejudge.common.model.entity.AnswerSubmission;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.core.cache.CacheQuestion;

/**
 * 根据代码执行的结果 给出答案提交的结果
 *
 * @author liuqiao
 * @since 2024-12-01
 */
public abstract class CoreJudgeStrategy {

    public JudgeInfo resolve(AnswerSubmission as, CacheQuestion cq) {
        final String language = as.getLanguage();
        switch (language) {
            case "java": {
                return javaResolve(as, cq);
            }
            case "cpp": {
                return cPlusResolve(as, cq);
            }
            case "c": {
                return cResolve(as, cq);
            }
            case "python": {
                return pythonResolve(as, cq);
            }
            case "go": {
                return goResolve(as, cq);
            }
        }

        return null;
    }

    public JudgeInfo javaResolve(AnswerSubmission as, CacheQuestion cq) {
        return null;
    }

    public JudgeInfo cPlusResolve(AnswerSubmission as, CacheQuestion cq) {
        return null;
    }

    public JudgeInfo cResolve(AnswerSubmission as, CacheQuestion cq) {
        return null;
    }

    public JudgeInfo pythonResolve(AnswerSubmission as, CacheQuestion cq) {
        return null;
    }

    public JudgeInfo goResolve(AnswerSubmission as, CacheQuestion cq) {
        return null;
    }
}
