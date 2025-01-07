package com.zyl.multiple.data.sources.controller;

import com.zyl.multiple.data.sources.pojo.mysql.User;
import com.zyl.multiple.data.sources.pojo.ms.UserTable;
import com.zyl.multiple.data.sources.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: multiple-data-sources
 * @description: 测试
 * @author: yl.zhan
 * @create: 2024-12-19 15:15
 **/
@RestController
public class TestController {

    @Autowired
    UserService userService;


    @GetMapping("getAllUser")
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("getAllUserTable")
    public List<UserTable> getAllUserTable(){
        return userService.getAllUserTable();
    }

    // 这里为了测试使用 get 方法
    @GetMapping("addUser")
    public String addUser(){
        return userService.testMyAndMS();
    }


    @GetMapping("addUser2")
    public String addUser2(){
        return userService.testMyAndMS2();
    }

    @GetMapping("addUser3")
    public String addUser3(){
        return userService.testMyAndMS3();
    }

    @GetMapping("addUser4")
    public String addUser4(){
        return userService.testMyAndMS4();
    }

    @GetMapping("addUser5")
    public String addUser5(){
        return userService.testMyAndMS5();
    }


}
