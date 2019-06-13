

package com.power.validator;

import cn.hutool.core.util.StrUtil;
import com.power.validator.context.ContextValidator;
import com.power.validator.util.LoggerUtil;
import com.power.validator.valid.BaseValidator;
import com.power.validator.valid.ValidatorFactory;
import com.power.validator.valid.ValidatorResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wwupower
 * @Title: 校验器工厂类
 * @history 2019年05月16日
 * @since JDK1.8
 */
public class ParamsValidatorFactory {

    public String userDefindVlidatorClasses;
    public static ValidatorFactory validatorFactory = new ValidatorFactory();

    public ParamsValidatorFactory(){
        super();
    }

    /**
     *
     * @param userDefindVlidatorClasses 自定义校验器的实现类 可以参考 正则或者长度的校验器使用
     */
    public ParamsValidatorFactory(String userDefindVlidatorClasses){
        this.userDefindVlidatorClasses = userDefindVlidatorClasses;
    }

    public ParamsValidator createParamsValidator() {
        ContextValidator contextValidator = ContextValidator.getInstance();
        try {
            if (StrUtil.isNotEmpty(userDefindVlidatorClasses)) {
                String[] userDefindVlidatorClassesArr = userDefindVlidatorClasses.split(",");
                for (String className : userDefindVlidatorClassesArr) {
                    BaseValidator validator = validatorFactory.createValidator(Class.forName(className));
                    //获取的解析器注解类型
                    //获取父类接口类型
                    Type type=validator.getClass().getGenericInterfaces()[0];
                    //ParameterizedType参数化类型，即泛型
                    ParameterizedType p=(ParameterizedType)type;
                    //getActualTypeArguments获取参数化类型的数组，泛型可能有多个,选择第一个
                    Class c=(Class) p.getActualTypeArguments()[0];
                    contextValidator.addUserDefinedValidator(c.getName(), validator);
                }
            }
        } catch (Exception ex) {
            LoggerUtil.error(ParamsValidatorFactory.class, "");
        }
        ParamsValidator paramsValidator2 = new ParamsValidator(contextValidator);
        return paramsValidator2;
    }

    public String getUserDefindVlidatorClasses() {
        return userDefindVlidatorClasses;
    }

    public ParamsValidatorFactory setUserDefindVlidatorClasses(String userDefindVlidatorClasses) {
        this.userDefindVlidatorClasses = userDefindVlidatorClasses;
        return this;
    }

}
