package com.mk.demoonspringboot.messaging.rabbitmq;

import com.mk.demoonspringboot.messaging.rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author Mark
 * @date 2020/10/29
 */
@Service
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void doSend(String exchangeName) {
        CorrelationData cd1 = new CorrelationData();
        rabbitTemplate.convertAndSend(exchangeName, "queue", "foo", cd1);
        //生产结果确认
        cd1.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("onFailure");
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(CorrelationData.Confirm confirm) {
                //经验证发送失败也可以回调到这里
                System.out.println("on success ack may be false here" + confirm.isAck());
            }
        });
    }
}
