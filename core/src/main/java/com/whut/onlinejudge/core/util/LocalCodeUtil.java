package com.whut.onlinejudge.core.util;

import cn.hutool.core.io.FileUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 代码编译 文件存储工具类
 * @author liuqiao
 * @since 2024-12-02
 */
public class LocalCodeUtil {

    private final static Runtime runtime = Runtime.getRuntime();

    public static boolean compile(String submittedCode, String coreCode, String submittedSrc, String coreCodeSrc, String dent) {
        // 保存源文件
        FileUtil.writeBytes(submittedCode.getBytes(StandardCharsets.UTF_8), submittedSrc);
        FileUtil.writeBytes(coreCode.getBytes(StandardCharsets.UTF_8), coreCodeSrc);


        // 编译
        final Process process;
        try {
            process = runtime.exec(String.format("javac -d %s -encoding utf-8 %s %s", dent, submittedSrc, coreCodeSrc));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            if (!process.waitFor(3000, TimeUnit.MILLISECONDS)) {
                FileUtil.del(submittedSrc);
                FileUtil.del(coreCodeSrc);
                FileUtil.del(dent);
                return false;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 删除源文件
        FileUtil.del(submittedSrc);
        FileUtil.del(coreCodeSrc);
        return true;
    }
}
