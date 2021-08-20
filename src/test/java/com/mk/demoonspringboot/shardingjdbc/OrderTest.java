package com.mk.demoonspringboot.shardingjdbc;

import com.mk.demoonspringboot.mybatis_plus.mapper.OrderItemMapper;
import com.mk.demoonspringboot.mybatis_plus.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

//@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Test
    public void addOrders() {
        for (int i = 10; i < 14; i++) {
            TOrder order = new TOrder();
            order.setOrderId(i);
            order.setUserId(i+1);
            orderMapper.insert(order);

            TOrderItem orderItem = new TOrderItem();
            orderItem.setOrderId(order.getOrderId());
            orderItemMapper.insert(orderItem);
        }
    }
}
