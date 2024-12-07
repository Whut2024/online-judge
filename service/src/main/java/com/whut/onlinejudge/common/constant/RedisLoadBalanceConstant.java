package com.whut.onlinejudge.common.constant;

/**
 * @author liuqiao
 * @since 2024-12-03
 */
public interface RedisLoadBalanceConstant {


    String MIN_HEAP_KEY = "oj:min-heap";

    String MACHINE_ID_NAME = "machineId";

    String EMPTY_RETURN = "-1";

    String MOCK_SIGN = "Hello World!";
    String TO_DOWN_MACHINE_ID_KEY = "oj:down";
    long MAX_FAIL_TIME = 3L;

    Double FAIL_HEIGHT = 1000_000D;


    String CHECK_FAIL_KEY = "oj:fail:check:";


    String FAIL_MACHINE_ID_KEY = "oj:fail-machine:";
    long FAIL_MACHINE_TIME = 2 * 60 * 1000L;
}
