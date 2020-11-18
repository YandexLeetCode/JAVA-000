package com.geekbang.springaop.annotation;

import org.springframework.stereotype.Repository;

/**
 * Created by yandex on 2020/11/16.
 */
@Repository(value = "userDao") // 相当于 xml 中 <bean id="userDao" class="" />
public class UserDaoImpl implements UserDao{
    @Override
    public void save() {
        System.out.println("userDao save!!");
    }
}
