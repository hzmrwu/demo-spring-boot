package com.mk.demoonspringboot.java.annotation;

import java.lang.annotation.*;

/**
 * @Author maokai.wu
 * @Date 2021/8/19
 * @Description 注解声明要导出的字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportableField {

    String value() default  "";

    Class<? extends Printer> serializer() default None.class;

    String pattern() default "";

    abstract class None implements Printer<Object> {
        public None() {
        }
    }

    interface Printer<T> {
        String print(T obj);
    }
}
