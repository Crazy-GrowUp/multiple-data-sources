package com.zyl.multiple.data.sources.controller;

import com.zyl.multiple.data.sources.pojo.User;
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

}
