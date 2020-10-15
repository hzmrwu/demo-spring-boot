package com.mk.demoonspringboot.mybatis_plus.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mk.demoonspringboot.mybatis_plus.entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        User user = new User();
        user.setAge(3);
        user.setEmail("123@qq.com");
        user.setId(33L);
        user.setName("Mark");
        int rows = userMapper.insert(user);
        Assert.assertEquals(1, rows);
        List<User> users = userMapper.selectList(null);
        Assert.assertTrue(users.size() > 0);

        User mark = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getName, "Mark").eq(User::getId, 3L));
        Assert.assertNull(mark);
        mark = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getName, "Mark").eq(User::getId, 33L));
        Assert.assertNotNull(mark);
    }

}