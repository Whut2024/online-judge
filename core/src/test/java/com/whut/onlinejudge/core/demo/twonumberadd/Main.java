package com.whut.onlinejudge.core.demo.twonumberadd;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author liuqiao
 * @since 2025-01-04
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

    private static void fail() {
        // 返回错误
        System.out.println(false);
        System.out.println(false);
    }


    /**
     * 当结果不符合预期时输出相关提示
     * <p>
     * 这个方法因该由题目提供者自定义
     */
    private static boolean checkResult(ListNode src, ListNode dent) {
        if (src == null && dent == null) {
            return true;
        }

        if (src != null && dent != null) {
            while (true) {
                if (src == null && dent == null) {
                    return true;
                }

                if (src == null || dent == null || src.val != dent.val) {
                    break;
                }

                src = src.next;
                dent = dent.next;
            }
        }

        return false;
    }


    public static void main(String[] args) throws ParseException {
        // 固定
        final Scanner scanner = new Scanner(System.in);
        memoryLimit = Integer.parseInt(scanner.next());
        timeLimit = Integer.parseInt(scanner.next());
        final int caseNumber = Integer.parseInt(scanner.next());
        final Solution solution = new Solution();


        // 变化//
        // target array-len number..... answer-number1 answer-number2
        final JSONParser parser = new JSONParser();

        final List<ListNode> l1List = new ArrayList<>(caseNumber);
        final List<ListNode> l2List = new ArrayList<>(caseNumber);
        final List<ListNode> answerListNodeList = new ArrayList<>(caseNumber);

        ListNode dummy = null;
        for (int i = 0; i < caseNumber; i++) {
            JSONArray jsonArray = (JSONArray) parser.parse(scanner.next());
            for (int j = jsonArray.size() - 1; j >= 0; j--) {
                dummy = new ListNode(((Long) jsonArray.get(j)).intValue(), dummy);
            }
            l1List.add(dummy);
            dummy = null;

            jsonArray = (JSONArray) parser.parse(scanner.next());
            for (int j = jsonArray.size() - 1; j >= 0; j--) {
                dummy = new ListNode(((Long) jsonArray.get(j)).intValue(), dummy);
            }
            l2List.add(dummy);
            dummy = null;
        }

        for (int i = 0; i < caseNumber; i++) {
            JSONArray jsonArray = (JSONArray) parser.parse(scanner.next());
            for (int j = jsonArray.size() - 1; j >= 0; j--) {
                dummy = new ListNode(((Long) jsonArray.get(j)).intValue(), dummy);
            }
            answerListNodeList.add(dummy);
            dummy = null;
        }

        startTime = System.currentTimeMillis();
        startMemory = runtime.freeMemory();
        try {
            for (int i = 0; i < caseNumber; i++) {
                if (!checkResult(solution.addTwoNumbers(l1List.get(i), l2List.get(i)), answerListNodeList.get(i))) {
                    fail();
                    return;
                }

                if (!checkMemory()) {
                    fail();
                    return;
                }

                if (!checkTime()) {
                    fail();
                    return;
                }
            }
        } catch (Throwable e) {
            occurException(e);
            return;
        }

        // 通过
        pass();
    }
}
