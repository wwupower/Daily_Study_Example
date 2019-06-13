package com.power.validator.annotation;

import java.lang.annotation.*;

/**
 * @author wwupower
 * @Title: 最大长度
 * @history 2019年05月15日
 * @since JDK1.8
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Max {
    long value();
    String msg() default "长度超出字符大小限制";
}
