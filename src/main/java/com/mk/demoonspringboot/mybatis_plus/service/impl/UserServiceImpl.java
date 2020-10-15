package com.mk.demoonspringboot.mybatis_plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mk.demoonspringboot.mybatis_plus.entity.User;
import com.mk.demoonspringboot.mybatis_plus.mapper.UserMapper;
import com.mk.demoonspringboot.mybatis_plus.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Mark
 * @date 2020/10/15
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService<User> {

    @Override
    public void customUserMethod() {
        log.info("call customUserMethod");
    }
}
