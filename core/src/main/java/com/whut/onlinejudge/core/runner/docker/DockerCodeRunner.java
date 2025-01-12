package com.whut.onlinejudge.core.runner.docker;

import com.whut.onlinejudge.core.cache.CacheQuestion;
import com.whut.onlinejudge.core.runner.CodeRunner;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 使用本地 Docker 服务器来运行代码
 *
 * @author liuqiao
 * @since 2024-12-01
 */
//@ConditionalOnBean
@Component
@AllArgsConstructor
@ConditionalOnBean(DockerExecutor.class)
public class DockerCodeRunner extends CodeRunner {

    private final DockerExecutor dockerExecutor;

    @Override
    protected List<String> executeUsersCode(String command, CacheQuestion cq) {
        // 输出-没有异常-未通过
        // 输出-异常-有异常-未通过
        // 输出-内存-时间-通过
        final String[] inputAll = cq.getInputAll();
        final String[] commandNoArgsArray = command.split(" ");
        final int ccaLen = commandNoArgsArray.length;

        final String[] commandArgsArray = new String[ccaLen + inputAll.length];
        System.arraycopy(commandNoArgsArray, 0, commandArgsArray, 0, ccaLen);
        System.arraycopy(inputAll, 0, commandArgsArray, ccaLen, inputAll.length);

        return dockerExecutor.execute(commandArgsArray);
    }
}
