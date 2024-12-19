package com.zyl.multiple.data.sources.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: multiple-data-sources
 * @description: 用户类
 * @author: yl.zhan
 * @create: 2024-12-19 15:35
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long id;

    private String name;

    private int age;

    private String email;

    private Date createDate;

    private Date updateDate;

    private int version;

    private int isDeleted;


}
