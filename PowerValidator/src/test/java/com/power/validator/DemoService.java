package com.power.validator;
import com.power.validator.annotation.*;

/**
 * @author wwupower
 * @Title: DemoService
 * @history 2019年06月13日
 * @since JDK1.8
 */
public class DemoService {

    public void findDemos(@NotNull(msg = "name不允许为空") String name, @ValidPojo DemoVO demoVO){
        System.out.println(name);
        System.out.println(demoVO.length);
    }
}
