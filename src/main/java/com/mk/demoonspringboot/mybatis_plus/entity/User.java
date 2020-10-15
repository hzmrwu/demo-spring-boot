package com.mk.demoonspringboot.mybatis_plus.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mark
 * @date 2020/10/15
 */
@Getter
@Setter
public class User {

    private Long id;

    private String name;

    private Integer age;

    private String email;
}
