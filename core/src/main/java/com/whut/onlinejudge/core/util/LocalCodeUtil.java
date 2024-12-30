package com.whut.onlinejudge.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.whut.onlinejudge.core.command.CommandFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 代码编译 文件存储工具类
 *
 * @author liuqiao
 * @since 2024-12-02
 */
public class LocalCodeUtil {

    private static final String OK = "";

    private static final String COMPILE_TIME_OUT = "编译超时";

    private final static Runtime runtime = Runtime.getRuntime();

    public static String compile(String language, String submittedCode, String coreCode, String submittedSrc, String coreCodeSrc, String dent, String prefix) {
        // 保存源文件
        FileUtil.writeBytes(submittedCode.getBytes(StandardCharsets.UTF_8), submittedSrc);
        FileUtil.writeBytes(coreCode.getBytes(StandardCharsets.UTF_8), coreCodeSrc);

        final String command = CommandFactory.getCompilationCommand(language);
        if (command == null) // 脚本语言
            return OK;

        // 编译
        final String compileCommand = String.format(command, prefix, submittedSrc, coreCodeSrc);
        final Process process;
        try {
            process = runtime.exec(compileCommand);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            if (!process.waitFor(3000, TimeUnit.MILLISECONDS)) {
                FileUtil.del(submittedSrc);
                FileUtil.del(coreCodeSrc);
                FileUtil.del(prefix);
                return COMPILE_TIME_OUT;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 编译失败
        final List<String> errorList = IoUtil.readLines(process.getErrorStream(), StandardCharsets.UTF_8, new ArrayList<>());
        if (CollectionUtil.isNotEmpty(errorList)) {
            final StringBuilder builder = new StringBuilder(128);
            final String start = errorList.get(0);
            builder.append(start.substring(start.indexOf("Solution.java")));
            for (int i = 1; i < errorList.size(); i++) {
                builder.append(errorList.get(i));
            }
            return builder.toString();
        }

        // 删除源文件
        FileUtil.del(submittedSrc);
        FileUtil.del(coreCodeSrc);
        return OK;
    }
}
