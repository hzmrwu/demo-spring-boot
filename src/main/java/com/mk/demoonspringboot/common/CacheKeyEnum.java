package com.mk.demoonspringboot.common;

import lombok.Getter;

@Getter
public enum CacheKeyEnum {

    INDUSTRY("industry", 3600 * 24),
    ;

    private final String prefix;

    /**
     * 缓存过期时间，秒
     */
    private final long expire;

    CacheKeyEnum(String prefix, long expire) {
        this.prefix = prefix;
        this.expire = expire;
    }
}
