package com.whut.onlinejudge.backgrounddoor;

import cn.hutool.dfa.WordTree;

/**
 * @author liuqiao
 * @since 2024-12-05
 */
public class DangerousWordTest {
    public static void main(String[] args) {
        WordTree wordTree = new WordTree();
        wordTree.addWords("File", "FileInputStream", "FileOutputStream", "FileReader",
                "FileWriter", "BufferedReader", "BufferedWriter", "RandomAccessFile", "Files", "Paths", "Path",
                "Thread", "Runnable", "ExecutorService", "ThreadPoolExecutor", "Callable", "FutureTask", "ThreadFactory",
                "File", "FileInputStream", "FileOutputStream", "FileReader", "FileWriter", "BufferedReader", "BufferedWriter",
                "RandomAccessFile", "Files", "Paths", "Path", "InputStream", "OutputStream", "ObjectInputStream",
                "ObjectOutputStream", "DataInputStream", "DataOutputStream", "PrintWriter", "FileChannel", "MappedByteBuffer",
                "WatchService", "PathMatcher", "FileSystem", "WatchKey", "Thread", "Runnable", "ExecutorService",
                "ThreadPoolExecutor", "Callable", "FutureTask", "ThreadFactory", "Timer",
                "TimerTask", "CountDownLatch", "CyclicBarrier", "Semaphore", "Exchanger",
                "Phaser", "ReentrantLock", "ReadWriteLock", "Condition", "AtomicInteger", "AtomicLong",
                "AtomicBoolean", "AtomicReference", "Runtime");

        String a = "aaRuntimsesss";
        System.out.println(wordTree.isMatch(a));
    }
}
