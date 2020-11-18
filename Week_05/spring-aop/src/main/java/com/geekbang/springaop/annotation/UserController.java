package com.geekbang.springaop.annotation;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Created by yandex on 2020/11/16.
 */
@Controller("userController") // <bean id="userController" class=""/>
public class UserController {
   @Resource(name = "userService") // <property name="userService" ref="userService"/>
   private UserService userService;

   public void setUserService(UserService userService) {
      this.userService = userService;
   }

   public void save() {
      userService.save();
      System.out.println("userController save");
   }
}
