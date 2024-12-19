package com.zyl.multiple.data.sources.service.impl;

import com.zyl.multiple.data.sources.mapper.UserMapper;
import com.zyl.multiple.data.sources.pojo.User;
import com.zyl.multiple.data.sources.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: multiple-data-sources
 * @description:
 * @author: yl.zhan
 * @create: 2024-12-19 15:46
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public List<User> getAllUser() {
        return userMapper.getAllUser();
    }
}
