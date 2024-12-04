package com.whut.onlinejudge.core;

/**
 * @author liuqiao
 * @since 2024-12-04
 */
public class MemoryTest {
    public static void main(String[] args) {
        final Runtime runtime = Runtime.getRuntime();
        final long start = runtime.freeMemory();
        //create();
        final byte[] bytes = new byte[1024 * 1024]; // 1KB

        System.out.println((start - runtime.freeMemory()) / 1024 / 1024);

    }

    public static void create() {
        final byte[] bytes = new byte[1024]; // 1KB
        System.out.println(bytes);
    }
}
