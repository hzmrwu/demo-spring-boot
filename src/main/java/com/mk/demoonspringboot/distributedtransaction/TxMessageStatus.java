package com.mk.demoonspringboot.distributedtransaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 消息状态
@Getter
@RequiredArgsConstructor
public enum TxMessageStatus {

    /**
     * 待处理
     */
    PENDING(0),

    /**
     * 成功
     */
    SUCCESS(1),

    /**
     * 处理失败
     */
    FAIL(-1),

    ;

    private final Integer status;
}
