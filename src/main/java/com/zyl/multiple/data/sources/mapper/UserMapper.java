package com.zyl.multiple.data.sources.mapper;

import com.zyl.multiple.data.sources.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: multiple-data-sources
 * @description:
 * @author: yl.zhan
 * @create: 2024-12-19 15:43
 **/
@Mapper
public interface UserMapper {
    List<User> getAllUser();
}
