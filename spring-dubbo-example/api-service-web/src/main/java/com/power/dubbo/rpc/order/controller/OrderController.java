package com.power.dubbo.rpc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.power.dubbo.rpc.order.mode.OrderVO;
import com.power.dubbo.rpc.order.service.IOrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wwupower
 * @Title: OrderController
 * @history 2019年05月11日
 * @since JDK1.8
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference(
            version = "1.0",
            application = "${dubbo.application.id}",
            registry = "${dubbo.registry.id}"
    )
    private IOrderService serviceIpml;



    @RequestMapping("/orderInfo/{id}")
    public OrderVO orderInfo(@PathVariable("id") Long id) {
        return serviceIpml.getOrderById(id);
    }

}
