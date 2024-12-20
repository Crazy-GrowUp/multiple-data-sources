package com.zyl.multiple.data.sources.service.impl;

import com.zyl.multiple.data.sources.mapper.mysql.UserMapper;
import com.zyl.multiple.data.sources.mapper.ms.UserTableMapper;
import com.zyl.multiple.data.sources.pojo.mysql.User;
import com.zyl.multiple.data.sources.pojo.ms.UserTable;
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

    private UserTableMapper userTableMapper;

    @Autowired
    public void setUserTableMapper(UserTableMapper userTableMapper) {
        this.userTableMapper = userTableMapper;
    }

    @Override
    public List<User> getAllUser() {
        return userMapper.getAllUser();
//        return new ArrayList<>();
    }

    @Override
    public List<UserTable> getAllUserTable() {
        return userTableMapper.getAllUserTables();
    }
}
