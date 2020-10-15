package com.mk.demoonspringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.mk.demoonspringboot.mybatis_plus.mapper")
@SpringBootApplication
public class DemoOnSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoOnSpringbootApplication.class, args);
	}

}
