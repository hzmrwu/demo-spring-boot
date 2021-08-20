package com.mk.demoonspringboot.localevent;

import com.mk.demoonspringboot.cache.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserListenerB {

    @EventListener(UserEvent.class)
    public void onUserChange(UserEvent userEvent) {
        User user = (User) userEvent.getSource();
        log.info("B listened user change " + user.getName() + " at thread " + Thread.currentThread().getName());
    }
}
