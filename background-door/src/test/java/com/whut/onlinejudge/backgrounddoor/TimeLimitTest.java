package com.whut.onlinejudge.backgrounddoor;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.nio.charset.StandardCharsets;

/**
 * @author liuqiao
 * @since 2024-12-05
 */
public class TimeLimitTest {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                while (true) {
                    HttpResponse response = HttpRequest.post("http://localhost:8101/api/user/register")
                            .header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                            .header("Content-Type", "application/json")
                            .header("Accept", "*/*")
                            .header("Host", "localhost:8101")
                            .header("Connection", "keep-alive")
                            .body("{\n    \"userAccount\": \"4\",\n    \"userAvatar\": \"https://avatars.githubusercontent.com/u/96811035\",\n    \"userName\": \"1\",\n    \"userPassword\": \"1\",\n    \"userRole\": \"admin\"\n}")
                            .execute();

                    System.out.println(new String(response.bodyBytes(), StandardCharsets.UTF_8));
                }
            }).start();
        }
    }
}
