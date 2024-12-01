package com.whut.onlinejudge.core.runner;

import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.JudgeInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * 使用本地 Docker 服务器来运行代码
 *
 * @author liuqiao
 * @since 2024-12-01
 */
//@ConditionalOnBean
@Component
public class DockerCodeRunner implements CodeRunner {
    @Override
    public JudgeInfo run(String submittedCode, String coreCode, JudgeConfig judgeConfig) {
        final JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemory(10);
        judgeInfo.setMessage("OK");
        judgeInfo.setTime(10);
        return judgeInfo;
    }
}
