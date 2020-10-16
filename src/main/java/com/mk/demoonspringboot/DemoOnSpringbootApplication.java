package com.mk.demoonspringboot;

import com.mk.demoonspringboot.mybatis_plus.mapper.UserMapper;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@MapperScan("com.mk.demoonspringboot.mybatis_plus.mapper")
@SpringBootApplication
public class DemoOnSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoOnSpringbootApplication.class, args);
	}

	@ApiOperation("测试接口")
	@GetMapping("/test")
	public String test() {
		return "http ok";
	}

	@Autowired
	private UserMapper userMapper;

	@ApiOperation("测试数据库")
	@GetMapping("/test-db")
	public Object testDb() {
		return userMapper.selectById(1L);
	}
}
