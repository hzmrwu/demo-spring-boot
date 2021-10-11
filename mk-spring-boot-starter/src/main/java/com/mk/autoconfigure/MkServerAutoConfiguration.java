package com.mk.autoconfigure;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;

@Configuration  //声明配置类
@EnableConfigurationProperties(MkServerProperties.class)    //使配置生效
public class MkServerAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(MkServerAutoConfiguration.class);

    private final MkServerProperties serverProperties;

    public MkServerAutoConfiguration(MkServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Bean //声明创建Bean
    @ConditionalOnClass(HttpServer.class)   //需要项目中存在HttpServer这个类。jdk自带的所以一定成立
    public HttpServer httpServer() throws IOException {
        // 创建 HttpServer 对象，并启动
        HttpServer server = HttpServer.create(new InetSocketAddress(serverProperties.getPort()), 0);
        server.start();
        logger.info("[httpServer][启动服务器成功，端口为:{}]", serverProperties.getPort());

        // 返回
        return server;
    }
}
