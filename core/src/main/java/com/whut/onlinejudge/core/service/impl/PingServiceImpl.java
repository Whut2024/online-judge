package com.whut.onlinejudge.core.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import com.whut.onlinejudge.common.model.entity.JudgeCase;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import com.whut.onlinejudge.common.model.enums.LanguageEnum;
import com.whut.onlinejudge.common.service.PingService;
import com.whut.onlinejudge.core.runner.CodeRunner;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * @author liuqiao
 * @since 2024-12-06
 */
@DubboService
@AllArgsConstructor
public class PingServiceImpl implements PingService {

    private final static String MOCK_SUBMITTED_CODE;

    private final static String MOCK_LANGUAGE;

    private final static String MOCK_CORE_CODE;

    private final static JudgeConfig MOCK_JUDGE_CONFIG;

    private final static List<JudgeCase> MOCK_JUDGE_CASE_LIST;

    static {
        MOCK_SUBMITTED_CODE = "class Solution { public String hello(String s) {System.out.println(\"Hello World!\"); return s;}}";
        MOCK_LANGUAGE = LanguageEnum.JAVA.getName();
        MOCK_CORE_CODE = "public class Main { public static void main(String[] args) {System.out.println(new Solution().hello());System.out.println(true);}}";
        JudgeConfig judgeConfig = new JudgeConfig();
        judgeConfig.setMemoryLimit(1000);
        judgeConfig.setMemoryLimit(1024 * 1024);
        MOCK_JUDGE_CONFIG = judgeConfig;
        MOCK_JUDGE_CASE_LIST = CollectionUtil.newArrayList(new JudgeCase(RedisLoadBalanceConstant.MOCK_SIGN,
                RedisLoadBalanceConstant.MOCK_SIGN));
    }

    private final CodeRunner runner;


    @Override
    public String check() {
        final JudgeInfo judgeInfo = runner.run(MOCK_LANGUAGE, MOCK_SUBMITTED_CODE, MOCK_CORE_CODE, MOCK_JUDGE_CONFIG, MOCK_JUDGE_CASE_LIST);
        return judgeInfo.getTime() > 0 ? judgeInfo.getMessage() : "";
    }
}
