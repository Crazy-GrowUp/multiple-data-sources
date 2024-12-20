package com.zyl.multiple.data.sources.mapper.ms;

import com.zyl.multiple.data.sources.pojo.ms.UserTable;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: multiple-data-sources
 * @description:
 * @author: yl.zhan
 * @create: 2024-12-19 16:41
 **/
@Mapper
public interface UserTableMapper {

    List<UserTable> getAllUserTables();

}
