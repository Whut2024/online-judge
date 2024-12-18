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
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            new Thread(() -> {
                while (true) {
                    long start = System.currentTimeMillis();
                    HttpResponse response = HttpRequest
                            .post("http://189.1.244.220:8101/api/answer_submission/do")
                            .header("token", "9836398af3a26cea9fe788849a0f88b8")
                            .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                            .header("Content-Type", "application/json")
                            .header("Accept", "*/*")
                            .header("Host", "localhost:8101")
                            .header("Connection", "keep-alive")
                            .body("{\"language\":\"java\",\"submittedCode\":\"import java.util.Random;\\n\\npublic class Solution {\\n\\n    public int add(int a, int b) {\\n        System.out.println(\\\"a = \\\" + a);\\n        System.out.println(\\\"b = \\\" + b);\\n        final byte[] bytes = new byte[1024]; // 1KB\\n        try {\\n            Thread.sleep(new Random().nextInt(10) * 200L);\\n        } catch (InterruptedException e) {\\n            throw new RuntimeException(e);\\n        }\\n        return a + b;\\n    }\\n}\",\"questionId\":\"1864232331863179266\"}")
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
                System.out.println("平均 QPS = " + (adder.sum() * 1000 / (System.currentTimeMillis() - processStart)))));
    }
}
