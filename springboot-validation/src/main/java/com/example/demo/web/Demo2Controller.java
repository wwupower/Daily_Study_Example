

package com.example.demo.web;

import com.example.demo.vo.Demo2;
import com.example.demo.vo.GroupA;
import com.example.demo.vo.GroupB;
import com.example.demo.vo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wwupower
 * @Title: DemoController
 * @history 2019年05月16日
 * @since JDK1.8
 */
@Controller
@RequestMapping("/demo")
public class Demo2Controller {

    @Autowired
    private Validator validator;

    @RequestMapping("/demo5")
    public void demo5(){
        Person p = new Person();
        /**GroupA验证不通过*/
        p.setUserId(-12);
        /**GroupA验证通过*/
        p.setUserName("a");
        p.setAge(110);
        p.setSex(5);
        Set<ConstraintViolation<Person>> validate = validator.validate(p, GroupA.class, GroupB.class);
        for (ConstraintViolation<Person> item : validate) {
            System.out.println(item);
        }
    }

    @RequestMapping("/demo6")
    @ResponseBody
    public String demo6(@Validated({GroupA.class, GroupB.class}) Person p, BindingResult result){
        StringBuffer d= new StringBuffer();
        if(result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error : allErrors) {
                System.out.println(error);
                d.append(error.getDefaultMessage());
            }
        }
        return d.toString();

    }


    @RequestMapping("/demo7")
    public void demo7(@Validated({GroupA.class}) Person p, BindingResult result){
        if(result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError error : allErrors) {
                System.out.println(error);
            }
        }
    }
}
