
package com.power.dubbo.rpc.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.power.dubbo.rpc.user.mode.UserVO;
import com.power.dubbo.rpc.user.service.IUserService;

/**
 * @author wwupower
 * @Title: OrderServiceIpml
 * @history 2019年05月11日
 * @since JDK1.8
 */
@Service(
        version = "1.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class UserServiceIpml implements IUserService {

    @Override
    public UserVO getUserById(String id) {
        UserVO orderVO = new UserVO();
        return orderVO.setId(1L).setName("张三");
    }
}
