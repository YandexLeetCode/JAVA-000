package com.geekbang.springaop.annotation;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by yandex on 2020/11/16.
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService{
    @Resource(name = "userDao") // xml <property name="userDao" ref="userDao"/>
    private UserDao userDao;

    @Override
    public void save() {
        userDao.save();
        System.out.println("UserService Save");
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
