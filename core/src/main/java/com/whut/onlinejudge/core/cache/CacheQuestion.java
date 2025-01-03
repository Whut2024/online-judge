package com.whut.onlinejudge.core.cache;

import com.whut.onlinejudge.common.model.entity.JudgeConfig;
import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuqiao
 * @since 2025-01-03
 */
@Data
public class CacheQuestion {

    private Long id;

    private ConcurrentHashMap<String, String> compiledCoreCodePathMap;

    private JudgeConfig judgeConfig;

    private String coreCode;

    private String baseCode;

    private String[] inputAll;

    public String getCompiledPath(String language) {
        return compiledCoreCodePathMap.get(language);
    }

    protected CacheQuestion() {}
}
