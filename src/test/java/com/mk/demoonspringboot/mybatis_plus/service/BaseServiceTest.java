package com.mk.demoonspringboot.mybatis_plus.service;

import com.mk.demoonspringboot.mybatis_plus.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseServiceTest {

    @Autowired
    private IUserService<User> userService;

    @Test
    public void commonMethodA() {
        userService.customUserMethod();
    }

    @Test
    public void saveBatch() {
        User a = new User();
        a.setId(1L);
        a.setName("u1");
        a.setAge(0);
        a.setEmail("e1");

        User b = new User();
        b.setId(2L);
        b.setName("u2");
        b.setAge(0);
        b.setEmail("e2");
        userService.saveBatch(Arrays.asList(a, b));
    }
}