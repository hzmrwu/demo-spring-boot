package com.mk.demoonspringboot.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImplV2 implements UserService {

    @Override
    public User addUser(User user) {
        return null;
    }

    // 使缓存失效
    @CacheEvict(value = "selectUser", key = "#id", beforeInvocation = false)
    @Override
    public User updateUser(Long id) {
        return null;
    }

    // 缓存结果key: selectUser::3
    @Cacheable(cacheNames = "selectUser", key = "#id")
    @Override
    public User selectUser(Long id) {
        log.info("selectUser nocache");
        User user = new User();
        user.setUserId(id);
        user.setName("name" +id);
        return user;
    }

    // 删除指定key: selectUser::3
    @CacheEvict(value = "selectUser", key = "#id", beforeInvocation = false)
    @Override
    public void deleteUser(Long id) {

    }
}
