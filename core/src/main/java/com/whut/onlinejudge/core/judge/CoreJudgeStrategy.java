package com.whut.onlinejudge.core.judge;

import com.whut.onlinejudge.common.model.entity.AnswerSubmission;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.common.model.entity.Question;

/**
 * 根据代码执行的结果 给出答案提交的结果
 *
 * @author liuqiao
 * @since 2024-12-01
 */
public abstract class CoreJudgeStrategy {

    public JudgeInfo resolve(AnswerSubmission as, Question q) {
        final String language = as.getLanguage();
        switch (language) {
            case "JAVA": {
                return javaResolve(as, q);
            }
            case "C_PLUS": {
                return cPlusResolve(as, q);
            }
            case "C": {
                return cResolve(as, q);
            }
            case "PYTHON": {
                return pythonResolve(as, q);
            }
            case "GO": {
                return goResolve(as, q);
            }
        }

        return null;
    }

    public JudgeInfo javaResolve(AnswerSubmission as, Question q) {
        return null;
    }

    public JudgeInfo cPlusResolve(AnswerSubmission as, Question q) {
        return null;
    }

    public JudgeInfo cResolve(AnswerSubmission as, Question q) {
        return null;
    }

    public JudgeInfo pythonResolve(AnswerSubmission as, Question q) {
        return null;
    }

    public JudgeInfo goResolve(AnswerSubmission as, Question q) {
        return null;
    }
}
