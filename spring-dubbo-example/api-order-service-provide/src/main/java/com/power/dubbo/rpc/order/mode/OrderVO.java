package com.power.dubbo.rpc.order.mode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wwupower
 * @Title: 订单信息
 * @history 2019年05月11日
 * @since JDK1.8
 */
public class OrderVO implements Serializable {

    private Long id;
    private String orderName;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public OrderVO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getOrderName() {
        return orderName;
    }

    public OrderVO setOrderName(String orderName) {
        this.orderName = orderName;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderVO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
