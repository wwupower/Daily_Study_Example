package com.power.controller;

import com.power.vo.UserVO;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * FeignClient
 */
@RestController
public class RestTemplateController {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RequestMapping(value = "/hello2/{name}", method = RequestMethod.GET)
    public String service1(@PathVariable("name") String name) {
        return restTemplate().getForObject("http://localhost:9000/hello?name="+name, String.class);
    }

    @RequestMapping("/getUserById2/{id}")
    public UserVO getUserById2(@PathVariable("id") String id) {
        return restTemplate().getForObject("http://localhost:9000/getUserById/"+id, UserVO.class);
    }



}