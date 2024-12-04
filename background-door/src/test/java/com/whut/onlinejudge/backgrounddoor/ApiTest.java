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
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    HttpResponse response = HttpRequest
                            .post("http://localhost:8101/api/answer_submission/do")
                            .header("token", "2a89acc01108576af790b8a270e02dc7")
                            .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                            .header("Content-Type", "application/json")
                            .header("Accept", "*/*")
                            .header("Host", "localhost:8101")
                            .header("Connection", "keep-alive")
                            .body("{\n    \"language\": \"JAVA\",\n    \"questionId\": \"1863821439531016194\",\n    \"submittedCode\": \"public class Solution {      public int add(int a, int b) {         System.out.println(\\\"a = \\\" + a);         System.out.println(\\\"b = \\\" + b);          return a + b;     } }\"\n}")
                            .execute();

                    System.out.println(new String(response.bodyBytes(), StandardCharsets.UTF_8));
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }
}
