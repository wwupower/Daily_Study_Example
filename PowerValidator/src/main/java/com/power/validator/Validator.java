

package com.power.validator;

import com.power.validator.valid.ValidatorResult;

import java.lang.reflect.Parameter;
import java.util.List;

/**
 * @author wwupower
 * @Title: Validator
 * @history 2019年05月16日
 * @since JDK1.8
 */
public interface Validator {
    /**
     * 实体对象校验
     * @param t
     * @param  clazz
     */
    List<ValidatorResult> validEntity(Object t,Class<?> clazz);

    /**
     * 执行方法参数校验
     * @param parameters
     */
    List<ValidatorResult> validMethod(Parameter[] parameters, Object... arg);

}
