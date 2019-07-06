package com.power.vo;

/**
 * @author wwupower
 * @Title: UserVO
 * @history 2019年07月06日
 * @since JDK1.8
 */
public class UserVO {

    private String id;
    private String name;
    private Integer age;

    public String getId() {
        return id;
    }

    public UserVO setId(String id) {
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

    public Integer getAge() {
        return age;
    }

    public UserVO setAge(Integer age) {
        this.age = age;
        return this;
    }
}
