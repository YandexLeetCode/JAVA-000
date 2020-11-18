package com.geekbang.springaop.pojo;
import java.util.List;

/**
 * Created by yandex on 2020/11/16.
 */
public class User {
    private String name;

    private List<String> list;
    private Integer age;


    public User() {
        super();
    };

    public User(String name,Integer age,List<String> list) {
        super();
        this.name = name;
        this.age = age;
        this.list = list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge (Integer age) {
        this.age = age;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "User [name = " + name +" age = " + age + " list = " + list + " ]";
    }
}

