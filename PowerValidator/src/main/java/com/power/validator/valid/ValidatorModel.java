

package com.power.validator.valid;

import java.lang.annotation.Annotation;

/**
 * @author wwupower
 * @Title: 注解内容和对应的校验器Model
 * @history 2019年05月17日
 * @since JDK1.8
 */
public class ValidatorModel {
    Annotation annotation;
    BaseValidator baseValidator;

    public Annotation getAnnotation() {
        return annotation;
    }

    public ValidatorModel setAnnotation(Annotation annotation) {
        this.annotation = annotation;
        return this;
    }

    public BaseValidator getBaseValidator() {
        return baseValidator;
    }

    public ValidatorModel setBaseValidator(BaseValidator baseValidator) {
        this.baseValidator = baseValidator;
        return this;
    }
}
