package com.whut.onlinejudge.backgrounddoor.dubbo.filter;

import com.whut.onlinejudge.backgrounddoor.common.ErrorCode;
import com.whut.onlinejudge.backgrounddoor.constant.DubboConstant;
import com.whut.onlinejudge.backgrounddoor.constant.RedisConstant;
import com.whut.onlinejudge.backgrounddoor.exception.BusinessException;
import com.whut.onlinejudge.common.constant.RedisLoadBalanceConstant;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.data.redis.core.StringRedisTemplate;

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


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        final Result result;
        try {
            result = invoker.invoke(invocation);
        } catch (RpcException e) {
            final String machineId = invoker.getUrl().getParameter(RedisLoadBalanceConstant.MACHINE_ID_NAME);
            final String key = RedisConstant.FAIL_MACHINE_ID_KEY + machineId;

            @SuppressWarnings("all") final long failTime = redisTemplate.opsForValue().increment(key);
            redisTemplate.expire(key, RedisConstant.FAIL_MACHINE_TIME, TimeUnit.MILLISECONDS);

            if (failTime > 3) // down the node assignment
                redisTemplate.opsForSet().add(RedisLoadBalanceConstant.TO_DOWN_MACHINE_ID_KEY, machineId);

            throw new BusinessException(ErrorCode.OPERATION_ERROR, "网络不稳定 请重试");
        }

        return result;
    }
}
