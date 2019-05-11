
package com.power.dubbo.rpc.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.power.dubbo.rpc.order.mode.OrderVO;
import com.power.dubbo.rpc.order.service.IOrderService;

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
public class OrderServiceIpml implements IOrderService {

    @Override
    public OrderVO getOrderById(Long id) {
        OrderVO orderVO = new OrderVO();
        return orderVO.setId(1L).setOrderName("测试");
    }
}
