package com.whut.onlinejudge.backgrounddoor.dubbo.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import com.whut.onlinejudge.backgrounddoor.constant.DubboConstant;
import com.whut.onlinejudge.backgrounddoor.constant.RedisConstant;
import com.whut.onlinejudge.backgrounddoor.exception.BusinessException;
import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.concurrent.TimeUnit;

/**
 * 当调用远程代码运行服务失败时执行
 *
 * @author liuqiao
 * @since 2024-12-06
 */
@Activate(group = DubboConstant.CONSUMER)
public class InvocationFailFilter implements Filter {

    public static StringRedisTemplate redisTemplate;

    private final static DefaultRedisScript<Void> INCREASE_FAIL_TIME_SCRIPT;

    static {
        INCREASE_FAIL_TIME_SCRIPT = new DefaultRedisScript<>();
        INCREASE_FAIL_TIME_SCRIPT.setLocation(new ClassPathResource("redis-lua/check_fail_number.lua"));
    }


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        final Result result;
        try {
            result = invoker.invoke(invocation);
        } catch (RpcException e) {
            final String machineId = invoker.getUrl().getParameter(RedisLoadBalanceConstant.MACHINE_ID_NAME);

            // 加锁读写过程操作过多
            /**
             * --- 异常次数加一
             * if redis.call('INCR', KEY[1]) == ARGV[3] + 1 then
             *     --- 异常次数超过预定值 加入节点到待下线节点
             *     redis.call('SADD', KEY[2], ARGV[1])
             * end
             * --- 刷新 key 超时时间
             * redis.call('EXPIRE', KEY[1], ARGV[2])
             */

            redisTemplate.execute(INCREASE_FAIL_TIME_SCRIPT,
                    CollectionUtil.newArrayList(RedisLoadBalanceConstant.FAIL_MACHINE_ID_KEY + machineId, // KEY[1]
                            RedisLoadBalanceConstant.TO_DOWN_MACHINE_ID_KEY), // KEY[2]
                    TimeUnit.MILLISECONDS.toSeconds(RedisLoadBalanceConstant.FAIL_MACHINE_TIME), // ARGV[1]
                    machineId, // ARGV[2]
                    RedisLoadBalanceConstant.MAX_FAIL_TIME); // ARGV[3]

            throw new BusinessException(ErrorCode.OPERATION_ERROR, "网络不稳定 请重试");
        }

        return result;
    }
}
