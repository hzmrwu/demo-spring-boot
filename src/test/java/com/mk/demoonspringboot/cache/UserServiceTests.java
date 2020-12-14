package com.mk.demoonspringboot.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void selectUser() {
        User user = userService.selectUser(666L);
        Assert.notNull(user, "user must not null");
    }

    @Test
    public void updateUser() {
        userService.updateUser(666L);
    }
}
