package com.zyl.multiple.data.sources.pojo.ms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: multiple-data-sources
 * @description:
 * @author: yl.zhan
 * @create: 2024-12-19 16:39
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTable {

    private long id;

    private String name;

    private int age;

    private String address;
}
