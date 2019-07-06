package com.power.service;

import com.power.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wwupower
 * @Title: 远程调用
 * @history 2019年07月06日
 * @since JDK1.8
 */
@FeignClient("spring-cloud-producer")
public interface UserSeriveClient {

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getUserById/{id}",method= RequestMethod.GET)
    UserVO getUserById(@PathVariable("id") String id);

}
