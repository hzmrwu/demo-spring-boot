package com.mk.demoonspringboot.cache;

public interface UserService {

    User addUser (User user) ;

    User updateUser (Long id) ;

    User selectUser (Long id);

    void deleteUser (Long id);
}
