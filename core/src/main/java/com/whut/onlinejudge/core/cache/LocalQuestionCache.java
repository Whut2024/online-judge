package com.whut.onlinejudge.core.cache;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import com.whut.onlinejudge.common.model.entity.Question;
import com.whut.onlinejudge.core.mapper.QuestionMapper;
import com.whut.onlinejudge.core.util.LocalCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuqiao
 * @since 2025-01-03
 */
@Slf4j
@Component
public class LocalQuestionCache {

    @Autowired
    private QuestionMapper qMapper;

    @Value("${code-runner.compiled-code-cache-prefix}")
    private String cachePrefix;

    private final ConcurrentHashMap<Long, CacheQuestion> cqMap;

    private final ReentrantLock queryLock;

    private final ReentrantLock compileLock;

    private final char LINE = '\n';

    {
        compileLock = new ReentrantLock();
        queryLock = new ReentrantLock();
        cqMap = new ConcurrentHashMap<>();
    }

    /**
     * <p>prepare question's core code, base code, judge config and input and output arrays</p>
     * out 内存 时间 测试案例个数 测试案例
     */
    private CacheQuestion get(Long id) {
        // loop util get a lock
        while (cqMap.get(id) == null && !queryLock.tryLock()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        CacheQuestion cq = cqMap.get(id);

        // double check
        if (cq != null) {
            return cq;
        }

        try {

            // get question from database
            final Question q = qMapper.getCoreCodeBaseCodeJudgeCaseJudgeConfig(id);
            cq = new CacheQuestion();
            if (q == null) {
                cqMap.put(id, cq);
                return cq;
            }

            cq.setId(id);
            cq.setBaseCode(q.getBaseCode());
            cq.setCoreCode(q.getCoreCode());
            final JudgeConfig judgeConfig = JSONUtil.toBean(q.getJudgeConfig(), JudgeConfig.class);
            cq.setJudgeConfig(judgeConfig);

            // prepare input array
            final List<String> inputAllList = new ArrayList<>();
            final List<String> outputList = new ArrayList<>();
            // add time and memory limit
            Collections.addAll(inputAllList, String.valueOf(judgeConfig.getMemoryLimit()), String.valueOf(judgeConfig.getTimeLimit()));
            final List<String> jcStr = JSONUtil.toList(q.getJudgeCase(), String.class);
            // add judge case number
            inputAllList.add(String.valueOf(jcStr.size()));
            for (String s : jcStr) {
                final JSONObject jsonObject = JSONUtil.parseObj(s);
                CollectionUtil.addAll(inputAllList, JSONUtil.toBean(jsonObject.getStr("input"), new TypeReference<List<String>>() {
                }, false));
                CollectionUtil.addAll(outputList, JSONUtil.toBean(jsonObject.getStr("output"), new TypeReference<List<String>>() {
                }, false));
            }

            CollectionUtil.addAll(inputAllList, outputList);

            inputAllList.replaceAll(s -> s + LINE);

            cq.setInputAll(inputAllList.toArray(new String[0]));
            cqMap.put(id, cq);
            return cq;
        } finally {
            // free lock
            queryLock.unlock();
        }
    }

    /**
     * compile core code and store it in local file system for different program language
     */
    public CacheQuestion get(Long id, String language) {
        final CacheQuestion cq = get(id);

        // loop util get a lock
        while (!compileLock.tryLock()) {
            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            ConcurrentHashMap<String, String> pathMap = cq.getCompiledCoreCodePathMap();
            if (pathMap == null) {
                pathMap = new ConcurrentHashMap<>();
                cq.setCompiledCoreCodePathMap(pathMap);
            }

            final String path = pathMap.get(language);
            if (StrUtil.isNotBlank(path)) {
                // it is not necessary to check whether this question exists
                return cq;
            }

            // just compile this core code and store the folder path
            final String coreCode = JSONUtil.parseObj(cq.getCoreCode()).getStr(language);
            if (StrUtil.isBlank(coreCode)) {
                log.error("{} 语言不存在", language);
                throw new RuntimeException("指定编程语言不存在");
            }
            final String coreLanguageCompiledPath = cachePrefix + File.separator + id + File.separator + language;
            LocalCodeUtil.compileCoreCode(language, cq.getBaseCode(), coreCode, coreLanguageCompiledPath);
            pathMap.put(language, coreLanguageCompiledPath);

            // help to gc the useless string
            cq.setCoreCode(null);
            cq.setBaseCode(null);

            return cq;
        } finally {
            // free lock
            compileLock.unlock();
        }


    }
}
