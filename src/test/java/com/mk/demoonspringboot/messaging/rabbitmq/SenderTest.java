package com.mk.demoonspringboot.messaging.rabbitmq;

import com.mk.demoonspringboot.messaging.rabbitmq.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SenderTest {


    @Autowired
    private Sender sender;

    @Autowired
    private Receiver receiver;

    @Test
    public void testSend() throws InterruptedException {
        sender.doSend(RabbitmqConfig.exchangeName);
        receiver.getLatch().await();
    }

    @Test
    public void testSendWithConfirm() throws InterruptedException {
        sender.doSend("notexistExchange");
    }
}