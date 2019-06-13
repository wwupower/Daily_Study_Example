

package com.power.validator;

import cn.hutool.core.util.ReflectUtil;
import com.power.validator.annotation.ValidPojo;
import com.power.validator.exception.ParamsValidatorException;
import com.power.validator.valid.BaseValidator;
import com.power.validator.context.ContextValidator;
import com.power.validator.valid.ValidatorModel;
import com.power.validator.valid.ValidatorResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wwupower
 * @Title: 参数校验器
 * @history 2019年05月14日
 * @since JDK1.8
 */
public class ParamsValidator implements Validator {
    /**
     * 是否快速校，默认是true，快速校验就是只要监测要一个校验不通过直接抛出异常
     */
    private boolean fastModel = true;
    public ContextValidator contextValidator;

    public ParamsValidator(ContextValidator contextValidator) {
        if(contextValidator == null){
            this.contextValidator = ContextValidator.getInstance();
        }else {
            this.contextValidator = contextValidator;
        }
    }
    public ParamsValidator() {
        super();
        contextValidator = ContextValidator.getInstance();
    }

    @Override
    public List<ValidatorResult> validEntity(Object t, Class<?> clazz) {
       return validField(t, clazz);
    }

    @Override
    public List<ValidatorResult> validMethod(Parameter[] parameters, Object... args) {
        List<ValidatorResult> validatorResults = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> paramClazz = parameter.getType();
            //基本类型的参数
            if (isPrimite(paramClazz)) {
                Annotation[] annotations = parameter.getAnnotations();
                Object value = args[i];
                String fieldName = parameter.getName();
                List<ValidatorModel> validatorModels = contextValidator.getValidator(parameter);
                validatorModels.forEach(validatorModel -> {
                    ValidatorResult validatorResult = valid(fieldName, value, validatorModel);
                    if (fastModel && !validatorResult.isSuccess()) {
                        throw new ParamsValidatorException(validatorResult.getMsg());
                    } else if (!validatorResult.isSuccess()) {
                        validatorResults.add(validatorResult);
                    }
                });
            } else {
                /*
                 * 忽略一些没有实体校验的注解
                 */
                if (parameter.getAnnotation(ValidPojo.class) == null) {
                    continue;
                }
                Object arg = Arrays.stream(args).filter(ar -> ar != null && paramClazz.isAssignableFrom(ar.getClass())).findFirst().get();
                List<ValidatorResult> validatorResults2 = validField(arg, paramClazz);
                validatorResults.addAll(validatorResults2);
            }
        }
        if (fastModel && validatorResults.size() > 0) {
            String msg = getErrorMsg(validatorResults);
            throw new ParamsValidatorException(msg);
        }
        return  validatorResults;
    }

    /**
     * 实体参数校验
     * @param t
     * @param tClass
     * @return
     */
    private List<ValidatorResult> validField(Object t, Class<?> tClass) {
        List<ValidatorResult> validatorResults = new ArrayList<>();
        //得到参数的所有成员变量
        Field[] declaredFields = tClass.getDeclaredFields();
        for (Field field : declaredFields) {
            String fieldName = field.getName();
            Object value = ReflectUtil.getFieldValue(t, field);
            List<ValidatorModel> validatorModels = contextValidator.getValidator(field);
            validatorModels.forEach(validatorModel -> {
                ValidatorResult validatorResult = valid(fieldName, value, validatorModel);
                if (fastModel && !validatorResult.isSuccess()) {
                    throw new ParamsValidatorException(validatorResult.getMsg());
                } else if (!validatorResult.isSuccess()) {
                    validatorResults.add(validatorResult);
                }
            });
        }
        if (fastModel && validatorResults.size() > 0) {
            String msg = getErrorMsg(validatorResults);
            throw new ParamsValidatorException(msg);
        }
        return validatorResults;
    }

    /**
     * 获取详细校验信息
     * @param validatorResults
     * @return
     */
    public String getErrorMsg(List<ValidatorResult> validatorResults) {
        StringBuffer msg = new StringBuffer();
        validatorResults.forEach(validatorResult -> {
            msg.append(",").append(validatorResult.getMsg());
        });
        return msg.length() > 1 ? msg.substring(1, msg.length()) : "";

    }

    /**
     * 校验
     * @param fieldName
     * @param value
     * @param validatorModel
     * @return
     */
    private ValidatorResult valid(String fieldName,Object value,ValidatorModel validatorModel) {
        BaseValidator validator = validatorModel.getBaseValidator();
        if (validator == null) {
            return new ValidatorResult().setSuccess(true);
        }
        ValidatorResult validatorResult = validator.valid(value, validatorModel.getAnnotation());
        if (!validatorResult.isSuccess()) {
            validatorResult.setMsg("参数[" + fieldName + "]" + validatorResult.getMsg());
        }
        return validatorResult;
    }


    public boolean isFastModel() {
        return fastModel;
    }

    public ParamsValidator setFastModel(boolean fastModel) {
        this.fastModel = fastModel;
        return this;
    }

    /**
     * 判断是否为基本类型：包括String
     *
     * @param clazz clazz
     * @return true：是;     false：不是
     */
    private boolean isPrimite(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == String.class;
    }
}




