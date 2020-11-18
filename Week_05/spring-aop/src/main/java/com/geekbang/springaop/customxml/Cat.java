package com.geekbang.springaop.customxml;

/**
 * Created by yandex on 2020/11/16.
 */
public class Cat implements Animals{
    private String name;
    private Integer age;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public void cry() {
        System.out.println("Cat is cry...");
    }
}
