package com.power.dubbo.rpc.user.service;


import com.power.dubbo.rpc.user.mode.UserVO;

/**
 * @author wwupower
 * @Title: IOrderService
 * @history 2019年05月11日
 * @since JDK1.8
 */
public interface IUserService {

    UserVO getUserById(String id);
}
