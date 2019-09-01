package com.power.validator;

import cn.hutool.core.util.ReflectUtil;
import com.power.validator.valid.ValidatorResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author wwupower
 * @Title: ValidatorTest
 * @history 2019年06月13日
 * @since JDK1.8
 */
public class ValidatorTest {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        //初始化参数校验工厂 并添加自定义校验器
        ParamsValidatorFactory paramsValidatorFactory = new ParamsValidatorFactory("com.power.validator.valid.NotEmptyValidator");
        //设置的快速检查模式
        ParamsValidator paramsValidator = paramsValidatorFactory.createParamsValidator().setFastModel(false);
        //单个实体对象检验
        List<ValidatorResult> validatorResults = paramsValidator.validEntity(new DemoVO().setLength("1").setReg("ac_ssasa").setMax("11212121"), DemoVO.class);
        validatorResults.forEach(validatorResult -> {
            System.out.println(validatorResult.getMsg());
        });
        DemoService demoService = new DemoService();
        //使用代理
        Method method = ReflectUtil.getMethod(demoService.getClass(), "findDemos", String.class, DemoVO.class);
        method.invoke(demoService, "aa", new DemoVO().setLength("1").setReg("ac_ssasa").setMax("11212121"));
        //方法级别校验
        List<ValidatorResult> validatorMenthodrResults = paramsValidator.validMethod(method.getParameters(),
                "",new DemoVO().setLength("122323").setReg("ac_ssasa").setMax("1"));
        System.out.println("---方法级别校验-");
        validatorMenthodrResults.forEach(validatorResult -> {
            System.out.println(validatorResult.getMsg());
        });
    }


}
