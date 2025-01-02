package com.whut.onlinejudge.core;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;

import java.util.List;

/**
 * @author liuqiao
 * @since 2025-01-02
 */
public class JsonObjectTest {
    public static void main(String[] args) {
        String data = "[[3,2,4],6]";

        List<String> list = JSONUtil.toBean(data, new TypeReference<List<String>>() {
        }, false);

        System.out.println(list);
    }
}
