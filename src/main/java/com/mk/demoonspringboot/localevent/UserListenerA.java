package com.mk.demoonspringboot.localevent;

import com.mk.demoonspringboot.cache.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserListenerA {

    @EventListener(UserEvent.class)
    public void onUserChange(UserEvent userEvent) {
        User user = (User) userEvent.getSource();
        log.info("a listened user change " + user.getName() + " at thread " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("listener A block publisher thread for 3 seconds");
    }
}
