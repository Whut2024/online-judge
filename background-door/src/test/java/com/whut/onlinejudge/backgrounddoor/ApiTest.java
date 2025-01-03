package com.whut.onlinejudge.backgrounddoor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author liuqiao
 * @since 2024-12-04
 */
public class ApiTest {
    public static void main(String[] args) {
        final long processStart = System.currentTimeMillis();
        final LongAdder adder = new LongAdder();

        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            new Thread(() -> {
                while (true) {
                    long start = System.currentTimeMillis();
                    HttpResponse response = HttpRequest
                            .post("http://189.1.244.220:8101/api/answer_submission/do")
                            .header("token", "17517c4af19dc6d10322c7c8a2937251")
                            .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                            .header("Content-Type", "application/json")
                            .header("Accept", "*/*")
                            .header("Host", "localhost:8101")
                            .header("Connection", "keep-alive")
                            .body("{\"language\":\"java\",\"submittedCode\":\"import java.util.*;\\nclass Solution {\\n    public int[] twoSum(int[] nums, int target) {\\n        HashMap<Integer, Integer> map = new HashMap<>();\\n        int need;\\n        for (int i = 0; i < nums.length; i++) {\\n            need = target - nums[i];\\n            if (map.containsKey(need)) return new int[] {i, map.get(need)};\\n            map.put(nums[i], i);\\n        }\\n\\n        return new int[0];\\n    }\\n}\",\"questionId\":\"1872929013556322306\"}")
                            .execute();

                    System.out.println(new String(response.bodyBytes(), StandardCharsets.UTF_8) + "请求消耗时间 " +
                            (System.currentTimeMillis() - start));
                    adder.increment();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                System.out.println("平均 TPS = " + (adder.sum() * 1000 / (System.currentTimeMillis() - processStart)))));
    }
}
