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

    Double FAIL_HEIGHT = 1000_000D;


    String CHECK_FAIL_KEY = "oj:fail:check:";
}
