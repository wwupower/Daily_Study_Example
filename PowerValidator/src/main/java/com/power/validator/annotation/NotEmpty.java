

package com.power.validator.annotation;

import java.lang.annotation.*;

/**
 * @author wwupower
 * @Title: 字符串不允许为空
 * @history 2019年05月15日
 * @since JDK1.8
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotEmpty {
    String msg() default "该字段不允许为空";
}
