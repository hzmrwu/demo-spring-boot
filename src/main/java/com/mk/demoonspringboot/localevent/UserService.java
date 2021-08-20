package com.mk.demoonspringboot.localevent;

import com.mk.demoonspringboot.cache.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void changeUser() {
        User user = new User();
        user.setName("mk");
        UserEvent event = new UserEvent(user);
        publisher.publishEvent(event);
        log.info("publishEvent user");
    }
}
