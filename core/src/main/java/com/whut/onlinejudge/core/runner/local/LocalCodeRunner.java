package com.whut.onlinejudge.core.runner.local;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import com.whut.onlinejudge.core.cache.CacheQuestion;
import com.whut.onlinejudge.core.runner.CodeRunner;
import com.whut.onlinejudge.core.runner.docker.DockerExecutor;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 直接在本地环境运行代码
 *
 * @author liuqiao
 * @since 2024-12-02
 */
@Component
@ConditionalOnMissingBean(DockerExecutor.class) // 兜底
@AllArgsConstructor
public class LocalCodeRunner extends CodeRunner {

    private final String FALSE = "false";

    @Override
    protected List<String> executeUsersCode(String command, CacheQuestion cq) {
        final Process process;
        try {
            ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", command);
            process = builder.start();
            // 设置参数相关的输入流
            OutputStream stdin = process.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(stdin, StandardCharsets.UTF_8));
            String[] inputAll = cq.getInputAll();
            for (String s : inputAll) {
                writer.write(s);
            }
            writer.write("");
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            if (!process.waitFor(cq.getJudgeConfig().getTimeLimit() * 4L, TimeUnit.MILLISECONDS)) {
                process.destroy();
                final List<String> list = new ArrayList<>();
                list.add("运行时间过长");
                list.add(FALSE);
                list.add(FALSE);
                return list;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 输出-没有异常-未通过
        // 输出-异常-有异常-未通过
        // 输出-内存-时间-通过
        final List<String> resList = IoUtil.readLines(process.getInputStream(), StandardCharsets.UTF_8, new ArrayList<>());
        final List<String> errorList = IoUtil.readLines(process.getErrorStream(), StandardCharsets.UTF_8, new ArrayList<>());
        final StringBuilder builder;
        if (CollectionUtil.isNotEmpty(errorList)) {
            builder = new StringBuilder(128);
            for (int i = 0; i < errorList.size() - 1; i++) {
                builder.append(errorList.get(i)).append('\n');
            }
            resList.set(resList.size() - 3, builder.toString());
        }

        return resList;
    }
}
