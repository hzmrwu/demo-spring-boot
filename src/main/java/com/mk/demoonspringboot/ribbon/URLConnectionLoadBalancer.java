package com.mk.demoonspringboot.ribbon;

import com.google.common.collect.Lists;
import com.netflix.client.ClientFactory;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import com.netflix.client.config.IClientConfigKey;
import com.netflix.loadbalancer.*;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import rx.Observable;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class URLConnectionLoadBalancer {

    private final ILoadBalancer loadBalancer;
    // retry handler that does not retry on same server, but on a different server
    //private final RetryHandler retryHandler = new DefaultLoadBalancerRetryHandler(0, 1, true);

    public URLConnectionLoadBalancer(List<Server> serverList) {
        loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(serverList);
    }

    public String call(final String path) throws Exception {
        return LoadBalancerCommand.<String>builder()
                .withLoadBalancer(loadBalancer)
                .build()
                .submit(server -> {
                    URL url;
                    try {
                        url = new URL("https://" + server.getHost() + ":" + server.getPort() + path);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        return Observable.just(conn.getResponseMessage());
                    } catch (Exception e) {
                        return Observable.error(e);
                    }
                }).toBlocking().first();
    }

    public LoadBalancerStats getLoadBalancerStats() {
        return ((BaseLoadBalancer) loadBalancer).getLoadBalancerStats();
    }

    public static void main(String[] args) throws Exception {
        demo3();
    }

    public static void demo1() throws Exception {
        URLConnectionLoadBalancer urlLoadBalancer = new URLConnectionLoadBalancer(Lists.newArrayList(
                new Server("www.baidu.com", 443),
                new Server("www.taobao.com", 443),
                new Server("www.douban.com", 443)));
        for (int i = 0; i < 6; i++) {
            System.out.println(urlLoadBalancer.call("/"));
        }
        System.out.println("=== Load balancer stats ===");
        System.out.println(urlLoadBalancer.getLoadBalancerStats());
    }

    public static void demo2() throws Exception {
        IRule rule = new RoundRobinRule();
        BaseLoadBalancer lb = new BaseLoadBalancer(new DummyPing(), rule);
        lb.setServersList(Arrays.asList(
                new Server("www.baidu.com", 443),
                new Server("www.taobao.com", 443),
                new Server("www.douban.com", 443)));
        LoadBalancerContext lbc = new LoadBalancerContext(lb);
        for (int i = 0; i < 6; i++) {
            Server server = lbc.getServerFromLoadBalancer(null, null);
            URL url = new URL("https://" + server.getHost() + ":" + server.getPort() + "/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println(conn.getResponseMessage());
        }
    }

    public static void demo3() throws Exception {
        IRule rule = new AlwaysGetFirstRule();
        BaseLoadBalancer lb = new BaseLoadBalancer(new DummyPing(), rule);
        lb.setServersList(Arrays.asList(
                new Server("www.baidu.com", 443),
                new Server("www.taobao.com", 443),
                new Server("www.douban.com", 443)));
        LoadBalancerContext lbc = new LoadBalancerContext(lb);
        for (int i = 0; i < 6; i++) {
            Server server = lbc.getServerFromLoadBalancer(null, null);
            URL url = new URL("https://" + server.getHost() + ":" + server.getPort() + "/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println(conn.getResponseMessage());
        }
    }
}
