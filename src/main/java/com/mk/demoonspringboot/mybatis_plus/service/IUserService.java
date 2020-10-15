package com.mk.demoonspringboot.mybatis_plus.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Mark
 * @date 2020/10/15
 */
public interface IUserService<T> extends IService<T> {

    void customUserMethod();
}
