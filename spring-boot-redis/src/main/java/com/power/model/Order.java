package com.power.model;

import javax.persistence.*;

/**
 * @author wwupower
 * @Title: Order
 * @history 2019年09月01日
 * @since JDK1.8
 */
@Entity
@Table(name="t_order")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String orderNo;


    public Order() {
    }

    public Long getId() {
        return id;
    }

    public Order setId(Long id) {
        this.id = id;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public Order setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }
}
