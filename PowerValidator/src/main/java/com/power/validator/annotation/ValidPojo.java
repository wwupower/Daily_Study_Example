package com.power.validator.annotation;

import java.lang.annotation.*;

/**
 * 实体的校验需要此注解
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidPojo {
    
}