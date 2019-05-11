package com.power.dubbo.rpc.user.mode;

import java.io.Serializable;

/**
 * @author wwupower
 * @Title: 订单信息
 * @history 2019年05月11日
 * @since JDK1.8
 */
public class UserVO implements Serializable {

    private Long id;
    private String name;
    private String sex;

    public Long getId() {
        return id;
    }

    public UserVO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserVO setName(String name) {
        this.name = name;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public UserVO setSex(String sex) {
        this.sex = sex;
        return this;
    }
}
