package com.example.demo.web;

import com.example.demo.service.DemoService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RequestMapping("/validation")
@RestController
@Validated
public class ValidationController {

    @Autowired
    DemoService demoService;

    /**如果只有少数对象，直接把参数写到Controller层，然后在Controller层进行验证就可以了。*/
    @RequestMapping(value = "/demo3", method = RequestMethod.GET)
    public void demo3(@Range(min = 1, max = 9, message = "年级只能从1-9")
                      @RequestParam(name = "grade", required = true)
                      int grade,
                      @Min(value = 1, message = "班级最小只能1")
                      @Max(value = 99, message = "班级最大只能99")
                      @RequestParam(name = "classroom", required = true)
                      int classroom) {

        System.out.println(grade + "," + classroom);
    }


    @RequestMapping(value = "/demo4", method = RequestMethod.GET)
    public void demo3() {
        demoService.demo(11+"",null);
    }

}