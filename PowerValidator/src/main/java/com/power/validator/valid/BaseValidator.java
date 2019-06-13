

package com.power.validator.valid;

import java.lang.annotation.Annotation;

/**
 * @author wwupower
 * @Title: 基础校验器
 * @history 2019年05月16日
 * @since JDK1.8
 */
public interface BaseValidator<A extends Annotation, T> {

    ValidatorResult valid(T value, A annotation);

}
