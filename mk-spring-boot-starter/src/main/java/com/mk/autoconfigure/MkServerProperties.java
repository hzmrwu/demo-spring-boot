package com.mk.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mk.server")
public class MkServerProperties {

    public static final int DEFAULT_PORT = 9090;

    private Integer port = DEFAULT_PORT;


    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
