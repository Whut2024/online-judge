package com.whut.onlinejudge.core.demo;

import java.util.Random;

public class Solution {

    public int add(int a, int b) {
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        final byte[] bytes = new byte[1024]; // 1KB
        try {
            Thread.sleep(new Random().nextInt(10) * 200L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return a + b;
    }
}
