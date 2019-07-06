package com.power.controller;

import com.power.service.UserSeriveClient;
import com.power.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FeignClient
 */
@RestController
public class ConsumerController {

    @Autowired
    com.power.service.HelloService HelloRemote;

    @Autowired
    UserSeriveClient userSeriveClient;
	
    @RequestMapping("/hello/{name}")
    public String index(@PathVariable("name") String name) {
        return HelloRemote.hello(name);
    }

    @RequestMapping("/getUserById/{id}")
    public UserVO getUserById(@PathVariable("id") String id) {
        return userSeriveClient.getUserById(id);
    }

}