package com.mk.demoonspringboot.common;

import com.mk.demoonspringboot.DemoOnSpringbootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoOnSpringbootApplication.class)
public class RestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testGet() {
        String result = restTemplate.getForObject("https://www.baidu.com", String.class);
        System.out.println(result);
    }
}
