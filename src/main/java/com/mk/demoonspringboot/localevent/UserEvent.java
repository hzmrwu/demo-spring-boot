package com.mk.demoonspringboot.localevent;

import com.mk.demoonspringboot.cache.User;
import org.springframework.context.ApplicationEvent;

public class UserEvent extends ApplicationEvent {

    public UserEvent(Object user) {
        super(user);
    }
}
