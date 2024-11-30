package com.whut.onlinejudge.backgrounddoor.dubbo.loadbalance;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

import java.util.List;

/**
 * @author liuqiao
 * @since 2024-11-30
 */
public class CurrentLeastUsageLoadBalance extends AbstractLoadBalance {
    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        for (Invoker<T> invoker : invokers) {
            final URL invokerUrl = invoker.getUrl();
            System.out.println(invokerUrl.getParameter("id-number"));
        }
        return null;
    }
}
