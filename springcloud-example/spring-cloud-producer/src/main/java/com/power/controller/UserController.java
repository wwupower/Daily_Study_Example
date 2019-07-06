package com.power.controller;

import com.power.vo.UserVO;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    /**
     * @param id
     * @return
     */
    @GetMapping("/getUserById/{id}")
    public UserVO getUserById(@PathVariable String id) {
        return new UserVO().setId(id).setAge(18).setName("这是一个18岁的小男孩 this 9000");
    }
}