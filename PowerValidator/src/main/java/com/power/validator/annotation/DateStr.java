package com.power.validator.annotation;

import java.lang.annotation.*;
/**
 * @author wwupower
 * @Title: 时间校验器
 * @history 2019年05月15日
 * @since JDK1.8
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DateStr {
    String[] format() default {"yyyy-MM-dd HH:mm:ss"};
    String msg() default "请传入符合时间格式的字符串,如：yyyy-MM-dd HH:mm:ss";
}
