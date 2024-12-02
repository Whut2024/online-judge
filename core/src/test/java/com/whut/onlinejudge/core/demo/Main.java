package com.whut.onlinejudge.core.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuqiao
 * @since 2024-12-02
 */
public class Main {

    public static void main(String[] args) {
        // 固定
        final int memoryLimit = Integer.parseInt(args[0]);
        final int timeLimit = Integer.parseInt(args[1]);
        final int caseNumber = Integer.parseInt(args[2]);
        final Solution solution = new Solution();

        // 变化
        final List<Integer> inList = new ArrayList<>(caseNumber);
        final List<Integer> outList = new ArrayList<>(caseNumber);
        for (int i = 0; i < caseNumber; i++) {
            inList.add(Integer.parseInt(args[3 + 2 * i]));
            outList.add(Integer.parseInt(args[4 + 2 * i]));
        }

        //
        final long start = System.currentTimeMillis();
        final Runtime runtime = Runtime.getRuntime();
        final long startMemory = runtime.freeMemory();
        try {
            for (int i = 0; i < caseNumber; i++) {
                final long freedMemory = runtime.freeMemory();
                if (solution.add(inList.get(i)) != outList.get(i)) {
                    // 返回错误
                    System.out.println(false);
                    System.out.println(false);
                    return;
                }

                if ((freedMemory - runtime.freeMemory()) / 1024 / 1024 > memoryLimit) {
                    // 内存溢出
                    System.out.println("内存溢出");
                    System.out.println(false);
                    System.out.println(false);
                }

                if ((System.currentTimeMillis() - start) / 1000 > timeLimit) {
                    // 运行超时
                    System.out.println("运行超时");
                    System.out.println(false);
                    System.out.println(false);
                }
            }
        } catch (Throwable e) {
            // 出现异常
            System.out.println(e);
            System.out.println(true);
            System.out.println(false);
            return;
        }

        // 通过
        System.out.println((startMemory - runtime.freeMemory()) / 1024 / 1024 / caseNumber);
        System.out.println((System.currentTimeMillis() - start) / caseNumber);
        System.out.println(true);


    }
}
