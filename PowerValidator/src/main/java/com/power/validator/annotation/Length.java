package com.power.validator.annotation;

import java.lang.annotation.*;

/**
 * @author wwupower
 * @Title: 长度校验
 * @history 2019年05月15日
 * @since JDK1.8
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Length {

    long min();
    long max();
    String msg() default "字符长度不符合要求";
}
