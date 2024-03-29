package com.mk.demoonspringboot.distributedtransaction;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mk.demoonspringboot.mybatis_plus.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionalMessageManagementService {

    private static final long DEFAULT_INIT_BACKOFF = 10L;
    private static final int DEFAULT_BACKOFF_FACTOR = 2;
    private static final int DEFAULT_MAX_RETRY_TIMES = 5;
    private static final LocalDateTime NEVER_TIME = LocalDateTime.of(2999, 1, 1, 0, 0, 0);

    private final RabbitTemplate rabbitTemplate;

    private TransactionalMessageMapper messageMapper;

    private TransactionalMessageContentMapper contentMapper;

    public void saveTransactionalMessageRecord(TransactionalMessage record, String content) {
        record.setMessageStatus(TxMessageStatus.PENDING.getStatus());//待处理
        record.setNextScheduleTime(calculateNextScheduleTime(LocalDateTime.now(), DEFAULT_INIT_BACKOFF,
                DEFAULT_BACKOFF_FACTOR, 0));
        record.setCurrentRetryTimes(0);
        record.setInitBackoff(DEFAULT_INIT_BACKOFF);
        record.setBackoffFactor(DEFAULT_BACKOFF_FACTOR);
        record.setMaxRetryTimes(DEFAULT_MAX_RETRY_TIMES);
        messageMapper.insert(record);
        TransactionalMessageContent messageContent = new TransactionalMessageContent();
        messageContent.setContent(content);
        messageContent.setMessageId(record.getId());
        contentMapper.insert(messageContent);
    }

    /**
     * 计算下一次执行时间
     *
     * @param base          基础时间
     * @param initBackoff   退避基准值
     * @param backoffFactor 退避指数
     * @param round         轮数
     * @return LocalDateTime
     */
    private LocalDateTime calculateNextScheduleTime(LocalDateTime base,
                                                    long initBackoff,
                                                    long backoffFactor,
                                                    long round) {
        double delta = initBackoff * Math.pow(backoffFactor, round);
        return base.plusSeconds((long) delta);
    }

    public void sendMessageSync(TransactionalMessage record, String content) {
        try {
            rabbitTemplate.convertAndSend(record.getExchangeName(), record.getRoutingKey(), content);
            if (log.isDebugEnabled()) {
                log.debug("发送消息成功,目标队列:{},消息内容:{}", record.getQueueName(), content);
            }
            // 标记成功
            markSuccess(record);
        } catch (Exception e) {
            // 标记失败
            markFail(record, e);
        }
    }

    private void markSuccess(TransactionalMessage record) {
        // 标记下一次执行时间为最大值
        record.setNextScheduleTime(NEVER_TIME);
        record.setCurrentRetryTimes(record.getCurrentRetryTimes().compareTo(record.getMaxRetryTimes()) >= 0 ?
                record.getMaxRetryTimes() : record.getCurrentRetryTimes() + 1);
        record.setMessageStatus(TxMessageStatus.SUCCESS.getStatus());
        record.setEditTime(LocalDateTime.now());
        messageMapper.updateById(record);
    }

    private void markFail(TransactionalMessage record, Exception e) {
        log.error("发送消息失败,目标队列:{}", record.getQueueName(), e);
        record.setCurrentRetryTimes(record.getCurrentRetryTimes().compareTo(record.getMaxRetryTimes()) >= 0 ?
                record.getMaxRetryTimes() : record.getCurrentRetryTimes() + 1);
        // 计算下一次的执行时间
        LocalDateTime nextScheduleTime = calculateNextScheduleTime(
                record.getNextScheduleTime(),
                record.getInitBackoff(),
                record.getBackoffFactor(),
                record.getCurrentRetryTimes()
        );
        record.setNextScheduleTime(nextScheduleTime);
        record.setMessageStatus(TxMessageStatus.FAIL.getStatus());
        record.setEditTime(LocalDateTime.now());
        messageMapper.updateById(record);
    }

    /**
     * 推送补偿 - 里面的参数应该根据实际场景定制
     */
    public void processPendingCompensationRecords() {
        // 时间的右值为当前时间减去退避初始值，这里预防把刚保存的消息也推送了
        LocalDateTime max = LocalDateTime.now().plusSeconds(-DEFAULT_INIT_BACKOFF);
        // 时间的左值为右值减去1小时
        LocalDateTime min = max.plusHours(-1);
        Map<Long, TransactionalMessage> collect = messageMapper.selectList(
                new QueryWrapper<TransactionalMessage>().lambda()
                        .between(TransactionalMessage::getNextScheduleTime, min, max)
                        .eq(TransactionalMessage::getMessageStatus, TxMessageStatus.PENDING.getStatus()))
                .stream()
                .collect(Collectors.toMap(TransactionalMessage::getId, x -> x));
        if (!collect.isEmpty()) {
            StringJoiner joiner = new StringJoiner(",", "(", ")");
            collect.keySet().forEach(x -> joiner.add(x.toString()));
            contentMapper.selectList(new QueryWrapper<TransactionalMessageContent>().lambda()
                    .in(TransactionalMessageContent::getMessageId, collect.keySet()))
                    .forEach(item -> {
                        //重新发送消息
                        TransactionalMessage message = collect.get(item.getMessageId());
                        sendMessageSync(message, item.getContent());
                    });
        }
    }
}
