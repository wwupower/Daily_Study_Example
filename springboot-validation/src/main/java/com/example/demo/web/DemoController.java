/******************************************************************************
 * Copyright (C) 2017 ShenZhen Powerdata Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳博安达开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.example.demo.web;

import com.example.demo.vo.Demo2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author wwupower
 * @Title: DemoController
 * @history 2019年05月16日
 * @since JDK1.8
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private Validator validator;

    @RequestMapping("/demo3")
    public void demo3(){
        Demo2 demo2 = new Demo2();
        demo2.setAge("111");
        demo2.setHigh(1500);
        demo2.setLength("ABCDE");
        demo2.setList(new ArrayList<String>(){{add("111");add("222");add("333");}});
        Set<ConstraintViolation<Demo2>> violationSet = validator.validate(demo2);
        for (ConstraintViolation<Demo2> model : violationSet) {
            System.out.println(model.getMessage());
        }
    }

    @RequestMapping("/demo2")
    public void demo2(@RequestBody @Valid Demo2 demo, BindingResult result){
        if(result.hasErrors()){
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
        }
    }
}
