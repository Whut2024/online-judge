package com.whut.onlinejudge.core.demo.twonumbersum;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

import java.util.*;

/**
 * @author liuqiao
 * @since 2024-12-28
 */
public class Main {

    private static int memoryLimit;

    private static int timeLimit;

    private static final Runtime runtime = Runtime.getRuntime();

    private static long startMemory;

    private static long startTime;

    /**
     * 检查当前的内存消耗是否大于最大限制内存，当大于最大限制内存时会输出相关的提示
     */
    private static boolean checkMemory() {
        if ((startMemory - runtime.freeMemory()) / 1024 > memoryLimit) {
            // 内存溢出
            System.out.println("内存溢出");
            System.out.println(false);
            System.out.println(false);
            return false;
        }
        return true;
    }

    /**
     * 检查当前的时间消耗是否大于最大限制时间，当大于最大限制时间时会输出相关的提示
     */
    private static boolean checkTime() {
        if (System.currentTimeMillis() - startTime > timeLimit) {
            // 运行超时
            System.out.println("运行超时");
            System.out.println(false);
            System.out.println(false);
            return false;
        }
        return true;
    }

    /**
     * 当出现异常时输出相关提示
     */
    private static void occurException(Throwable e) {
        // 出现异常
        e.printStackTrace(System.err);
        System.out.println(" ");
        System.out.println(true);
        System.out.println(false);
    }

    /**
     * 测试通过时输出相关的内存消耗和时间消耗
     */
    private static void pass() {
        long useMemory = (startMemory - runtime.freeMemory()) / 1024;
        if (useMemory == 0) {
            useMemory = (long) (Math.random() * 100);
        }
        System.out.println(useMemory);
        System.out.println(System.currentTimeMillis() - startTime);
        System.out.println(true);
    }


    /**
     * 当结果不符合预期时输出相关提示
     * <p>
     * 这个方法因该由题目提供者自定义
     */
    private static boolean checkResult(int[] src, int[] dent) {
        Arrays.sort(src);
        if (Arrays.equals(src, dent)) {
            return true;
        }

        // 返回错误
        System.out.println(false);
        System.out.println(false);
        return false;
    }


    public static void main(String[] args) {
        // 固定
        final Scanner scanner = new Scanner(System.in);
        memoryLimit = Integer.parseInt(scanner.next());
        timeLimit = Integer.parseInt(scanner.next());
        final int caseNumber = Integer.parseInt(scanner.next());
        final Solution solution = new Solution();


        // 变化//
        // target array-len number..... answer-number1 answer-number2
        final int[][] numsArray = new int[caseNumber][];
        final int[] targetArray = new int[caseNumber];
        final int[][] answerArray = new int[caseNumber][2];

        for (int i = 0; i < caseNumber; i++) {
            String next = scanner.next();
            numsArray[i] = JSON.parseObject(next, new TypeReference<int[]>() {});
            next = scanner.next();
            targetArray[i]= Integer.parseInt(next);
        }

        for (int i = 0; i < caseNumber; i++) {
            String next = scanner.next();
            answerArray[i] = JSON.parseObject(next, new TypeReference<int[]>() {});
        }

        startTime = System.currentTimeMillis();
        startMemory = runtime.freeMemory();
        try {
            for (int i = 0; i < caseNumber; i++) {
                int[] src = solution.twoSum(numsArray[i], targetArray[i]);
                if (!checkResult(src, answerArray[i]))
                    return;

                if (!checkMemory())
                    return;

                if (!checkTime())
                    return;
            }
        } catch (Throwable e) {
            occurException(e);
            return;
        }

        // 通过
        pass();
    }
}
