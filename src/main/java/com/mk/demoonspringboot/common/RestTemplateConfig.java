package com.mk.demoonspringboot.common;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean("restTemplate")
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient());
        return new RestTemplate(httpRequestFactory);
    }

    @Bean
    public HttpClient httpClient() {
        /*Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();*/
        //PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        //连接池最大连接数
        connectionManager.setMaxTotal(2000);
        //同路由最大连接数
        connectionManager.setDefaultMaxPerRoute(2000);

        RequestConfig requestConfig = RequestConfig.custom()
                //读取超时时间
                .setSocketTimeout(30 * 1000)
                //连接超时时间
                .setConnectTimeout(15 * 1000)
                //从连接池中获取连接的超时时间
                .setConnectionRequestTimeout(1000)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                //.setKeepAliveStrategy(keepAliveStrategy())
                .build();
    }

    /**
     * 配置Keep-Alive
     * @return
     */
    public ConnectionKeepAliveStrategy keepAliveStrategy() {
        return new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                Args.notNull(response, "HTTP response");
                final HeaderElementIterator it = new BasicHeaderElementIterator(
                        response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    final HeaderElement he = it.nextElement();
                    final String param = he.getName();
                    final String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch(final NumberFormatException ignore) {
                        }
                    }
                }
                return 30 * 1000L;
            }
        };
    }

}
