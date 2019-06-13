package com.power.validator.annotation;

import java.lang.annotation.*;

/**
 * @author wwupower
 * @Title: 正则表达式校验
 * @history 2019年05月15日
 * @since JDK1.8
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pattern {
    String regexp();
    String msg() default "不符合正则表达式";
}
