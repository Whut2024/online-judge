package com.whut.onlinejudge.common.service;

/**
 * 监控服务器调用代码运行服务器相关接口来检测代码运行服务是否正常
 *
 * @author liuqiao
 * @since 2024-12-06
 */
public interface PingService {

    /**
     * 当运行代码服务正常时会返回字符串 "Hello World!"
     */
    String check();
}
