package com.mk.demoonspringboot.distributedtransaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitTransactionalMessageService implements TransactionalMessageService {

    private static final ConcurrentMap<String, Boolean> QUEUE_ALREADY_DECLARE = new ConcurrentHashMap<>();

    private final AmqpAdmin amqpAdmin;

    private final TransactionalMessageManagementService managementService;

    @Override
    public void sendTransactionalMessage(Destination destination, TxMessage message) {
        String queueName = destination.queueName();
        String exchangeName = destination.exchangeName();
        String routingKey = destination.routingKey();
        ExchangeType exchangeType = destination.exchangeType();
        // 原子性的预声明
        QUEUE_ALREADY_DECLARE.computeIfAbsent(queueName, k -> {
            Queue queue = new Queue(queueName);
            amqpAdmin.declareQueue(queue);
            Exchange exchange = new CustomExchange(exchangeName, exchangeType.getType());
            amqpAdmin.declareExchange(exchange);
            Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
            amqpAdmin.declareBinding(binding);
            return true;
        });
        TransactionalMessage record = new TransactionalMessage();
        record.setQueueName(queueName);
        record.setExchangeName(exchangeName);
        record.setExchangeType(exchangeType.getType());
        record.setRoutingKey(routingKey);
        record.setBusinessModule(message.businessModule());
        record.setBusinessKey(message.businessKey());
        String content = message.content();
        // 保存事务消息记录
        managementService.saveTransactionalMessageRecord(record, content);
        // 注册事务同步器
        //mk:这里发消息可能会失败，比如自己宕机了，所以后面要定时扫描补偿
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                managementService.sendMessageSync(record, content);
            }
        });
    }
}
