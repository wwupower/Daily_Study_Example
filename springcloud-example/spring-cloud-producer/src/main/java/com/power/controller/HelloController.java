package com.power.controller;

import com.power.vo.UserVO;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String index(@RequestParam String name) {
        return "hello " + name + "，这是个服务";
    }


}