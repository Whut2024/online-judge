package com.whut.onlinejudge.backgrounddoor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.nio.charset.StandardCharsets;

/**
 * @author liuqiao
 * @since 2024-12-04
 */
public class ApiTest {
    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
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
                            .header("token", "83f309477837817a1c52d045b7aa42e4")
                            .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                            .header("Content-Type", "application/json")
                            .header("Accept", "*/*")
                            .header("Host", "localhost:8101")
                            .header("Connection", "keep-alive")
                            .body("{\"language\":\"java\",\"submittedCode\":\"import java.util.Random;\\n\\npublic class Solution {\\n\\n    public int add(int a, int b) {\\n        System.out.println(\\\"a = \\\" + a);\\n        System.out.println(\\\"b = \\\" + b);\\n        final byte[] bytes = new byte[1024]; // 1KB\\n        try {\\n            Thread.sleep(new Random().nextInt(10) * 200L);\\n        } catch (InterruptedException e) {\\n            throw new RuntimeException(e);\\n        }\\n        return a + b;\\n    }\\n}\",\"questionId\":\"1864232331863179266\"}")
                            .execute();

                    System.out.println(new String(response.bodyBytes(), StandardCharsets.UTF_8) + "请求消耗时间 " +
                            (System.currentTimeMillis() - start));
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }
}
