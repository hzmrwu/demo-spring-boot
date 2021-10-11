package com.mk.demoonspringboot.distributedtransaction;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("t_transactional_message_content")
public class TransactionalMessageContent {

    private Long id;
    private Long messageId;
    private String content;
}
