

package com.power.validator.valid;

/**
 * @author wwupower
 * @Title: 校验器工厂类
 * @history 2019年05月17日
 * @since JDK1.8
 */
public class ValidatorFactory {

    /**
     * 创建校验器
     * @param validatorClass BaseValidator的实现类
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public BaseValidator createValidator(Class<?> validatorClass) throws IllegalAccessException, InstantiationException {
        return (BaseValidator)validatorClass.newInstance();
    }
}
