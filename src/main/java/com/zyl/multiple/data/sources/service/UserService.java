package com.zyl.multiple.data.sources.service;

import com.zyl.multiple.data.sources.pojo.mysql.User;
import com.zyl.multiple.data.sources.pojo.ms.UserTable;

import java.util.List;

/**
 * @program: multiple-data-sources
 * @description:
 * @author: yl.zhan
 * @create: 2024-12-19 15:45
 **/
public interface UserService {
    List<User> getAllUser();

    List<UserTable> getAllUserTable();
}
