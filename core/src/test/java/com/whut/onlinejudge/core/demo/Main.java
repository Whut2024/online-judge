package com.whut.onlinejudge.core.demo;

/**
 * @author liuqiao
 * @since 2024-12-02
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
        System.out.println(e);
        System.out.println(true);
        System.out.println(false);
    }

    /**
     * 当结果不符合预期时输出相关提示
     * <p>
     * 这个方法因该由题目提供者自定义
     */
    private static boolean checkResult(int src, int dent) {
        if (src != dent) {
            // 返回错误
            System.out.println(false);
            System.out.println(false);
            return false;
        }
        return true;
    }

    /**
     * 测试通过时输出相关的内存消耗和时间消耗
     */
    private static void pass(int caseNumber) {
        System.out.println((startMemory - runtime.freeMemory()) / 1024 / caseNumber);
        System.out.println((System.currentTimeMillis() - startTime) / caseNumber);
        System.out.println(true);
    }

    public static void main(String[] args) {
        // 固定
        memoryLimit = Integer.parseInt(args[0]);
        timeLimit = Integer.parseInt(args[1]);
        final int caseNumber = Integer.parseInt(args[2]);
        final Solution solution = new Solution();

        // 变化
        final int[] a = new int[caseNumber], b = new int[caseNumber], c = new int[caseNumber];
        for (int i = 0; i < caseNumber; i++) {
            a[i] = Integer.parseInt(args[3 * i + 3]);
            b[i] = Integer.parseInt(args[3 * i + 4]);
            c[i] = Integer.parseInt(args[3 * i + 5]);
        }

        startTime = System.currentTimeMillis();
        startMemory = runtime.freeMemory();
        try {
            for (int i = 0; i < caseNumber; i++) {
                if (!checkResult(solution.add(a[i], b[i]), c[i]))
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
        pass(caseNumber);
    }
}
