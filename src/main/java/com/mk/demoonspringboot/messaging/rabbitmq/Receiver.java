package com.mk.demoonspringboot.messaging.rabbitmq;

import com.mk.demoonspringboot.messaging.rabbitmq.config.RabbitmqConfig;
import lombok.Getter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @author Mark
 * @date 2020/10/29
 */
@Component
public class Receiver {

    @Getter
    private CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = RabbitmqConfig.queueName)
    public void listen(String in) {
        System.out.println("Message read from myQueue : " + in);
        latch.countDown();
    }
}
