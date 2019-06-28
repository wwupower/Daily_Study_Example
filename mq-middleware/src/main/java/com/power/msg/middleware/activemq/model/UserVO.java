package com.power.msg.middleware.activemq.model;

import java.io.Serializable;

/**
 * @author wwupower
 * @Title: UserVO
 * @history 2019年06月28日
 * @since JDK1.8
 */
public class UserVO implements Serializable {

    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public UserVO setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public UserVO setAge(Integer age) {
        this.age = age;
        return this;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
