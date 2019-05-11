package com.power.dubbo.rpc.order.service;

import com.power.dubbo.rpc.order.mode.OrderVO;

/**
 * @author wwupower
 * @Title: IOrderService
 * @history 2019年05月11日
 * @since JDK1.8
 */
public interface IOrderService {

    OrderVO getOrderById(Long id);
}
