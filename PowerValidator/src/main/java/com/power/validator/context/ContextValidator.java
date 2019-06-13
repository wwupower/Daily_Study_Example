

package com.power.validator.context;

import com.power.validator.annotation.*;
import com.power.validator.util.LoggerUtil;
import com.power.validator.valid.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wwupower
 * @Title: 存放相关注解对应的校验器
 * @history 2019年05月17日
 * @since JDK1.8
 */
public class ContextValidator {

    public static final Map<String, BaseValidator> contextConfig = new ConcurrentHashMap<>();
    public static ValidatorFactory validatorFactory = new ValidatorFactory();
    private static volatile ContextValidator contextValidator;

    public static ContextValidator getInstance() {
        if (contextValidator == null) {
            synchronized (ContextValidator.class) {
                if (contextValidator == null) {
                    contextValidator = new ContextValidator();
                }
            }
        }
        return contextValidator;
    }

    public ContextValidator() {
        super();
        init();
    }

    /**
     * 初始化系统默认校验器
     */
    private void init() {
        try {
            contextConfig.put(NotNull.class.getName(), validatorFactory.createValidator(NotNullValidator.class));
            contextConfig.put(DateStr.class.getName(), validatorFactory.createValidator(DateStrValidator.class));
            contextConfig.put(NotEmpty.class.getName(), validatorFactory.createValidator(NotEmptyValidator.class));
            contextConfig.put(Max.class.getName(), validatorFactory.createValidator(MaxValitdator.class));
            contextConfig.put(Min.class.getName(), validatorFactory.createValidator(MinValitdator.class));
            contextConfig.put(Length.class.getName(), validatorFactory.createValidator(LengthValitdator.class));
            contextConfig.put(Pattern.class.getName(), validatorFactory.createValidator(PatternValitdator.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加用户自定义校验器
     *
     * @param annotationClass
     * @param validator
     */
    public void addUserDefinedValidator(String annotationClass, BaseValidator validator) {
        contextConfig.put(annotationClass, validator);
    }

    public List<ValidatorModel> getValidator(Parameter parameter) {
        List<ValidatorModel> baseValidators = new ArrayList<>();
        try {
            Set<String> keys = contextConfig.keySet();
            for (String className : keys) {
                Class aClass = Class.forName(className);
                Annotation annotation = parameter.getAnnotation(aClass);
                if (annotation != null) {
                    BaseValidator validator = contextConfig.get(className);
                    ValidatorModel validatorModel = new ValidatorModel().setAnnotation(annotation).setBaseValidator(validator);
                    baseValidators.add(validatorModel);
                }
            }
        } catch (ClassNotFoundException ex) {
            LoggerUtil.error(this.getClass(), ex.getMessage(), ex);
        }
        return baseValidators;
    }

    public List<ValidatorModel> getValidator(Field field) {
        List<ValidatorModel> baseValidators = new ArrayList<>();
        try {
            Set<String> keys = contextConfig.keySet();
            for (String className : keys) {
                Class aClass = Class.forName(className);
                Annotation annotation = field.getAnnotation(aClass);
                if (annotation != null) {
                    BaseValidator validator = contextConfig.get(className);
                    ValidatorModel validatorModel = new ValidatorModel().setAnnotation(annotation).setBaseValidator(validator);
                    baseValidators.add(validatorModel);
                }
            }
        } catch (ClassNotFoundException ex) {
            LoggerUtil.error(this.getClass(), ex.getMessage(), ex);
        }
        return baseValidators;
    }

}
