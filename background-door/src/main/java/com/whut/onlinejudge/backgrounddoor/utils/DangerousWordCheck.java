package com.whut.onlinejudge.backgrounddoor.utils;

import cn.hutool.dfa.WordTree;
import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import com.whut.onlinejudge.backgrounddoor.exception.BusinessException;
import com.whut.onlinejudge.common.model.enums.LanguageEnum;

import java.util.HashMap;

/**
 * 检查用户提交的代码中是否有危险关键字或者相关类
 *
 * @author liuqiao
 * @since 2024-12-05
 */
public final class DangerousWordCheck {

    private final static HashMap<String, WordTree> languageWordTreeMap;

    static {
        languageWordTreeMap = new HashMap<>(16);

        languageWordTreeMap.put(LanguageEnum.JAVA.getName(), new WordTree().addWords("File", "FileInputStream", "FileOutputStream", "FileReader",
                "FileWriter", "BufferedReader", "BufferedWriter", "RandomAccessFile", "Files", "Paths", "Path",
                "Thread", "Runnable", "ExecutorService", "ThreadPoolExecutor", "Callable", "FutureTask", "ThreadFactory",
                "File", "FileInputStream", "FileOutputStream", "FileReader", "FileWriter", "BufferedReader", "BufferedWriter",
                "RandomAccessFile", "Files", "Paths", "Path", "InputStream", "OutputStream", "ObjectInputStream",
                "ObjectOutputStream", "DataInputStream", "DataOutputStream", "PrintWriter", "FileChannel", "MappedByteBuffer",
                "WatchService", "PathMatcher", "FileSystem", "WatchKey", "Thread", "Runnable", "ExecutorService",
                "ThreadPoolExecutor", "Callable", "FutureTask", "ThreadFactory", "Timer",
                "TimerTask", "CountDownLatch", "CyclicBarrier", "Semaphore", "Exchanger",
                "Phaser", "ReentrantLock", "ReadWriteLock", "Condition", "AtomicInteger", "AtomicLong",
                "AtomicBoolean", "AtomicReference", "Runtime", "Class"));

        languageWordTreeMap.put(LanguageEnum.C.getName(), new WordTree());

        languageWordTreeMap.put(LanguageEnum.C_PLUS.getName(), new WordTree());

        languageWordTreeMap.put(LanguageEnum.PYTHON.getName(), new WordTree());

        languageWordTreeMap.put(LanguageEnum.GO.getName(), new WordTree());
    }

    private DangerousWordCheck() {
    }

    public static boolean check(String language, String code) {
        final WordTree wordTree = languageWordTreeMap.get(language);
        if (wordTree == null)
            throw new BusinessException(ErrorCode.OPERATION_ERROR);

        return wordTree.isMatch(code);
    }
}
