package com.zyl.multiple.data.sources.service.impl;

import com.zyl.multiple.data.sources.mapper.mysql.UserMapper;
import com.zyl.multiple.data.sources.mapper.ms.UserTableMapper;
import com.zyl.multiple.data.sources.pojo.mysql.User;
import com.zyl.multiple.data.sources.pojo.ms.UserTable;
import com.zyl.multiple.data.sources.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;
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

    @Autowired
    private UserTableMapper userTableMapper;

    @Qualifier("sqlserverTransactionManager")
    private DataSourceTransactionManager sqlserverTransactionManager;

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String testMyAndMS() {
        // 下面 的语句不报错正常执行
        User user = new User();
        user.setId(new Date().getTime());
        user.setName("小红");
        user.setAge(22);
        UserTable userTable = new UserTable();
        userTable.setName("小铁");
        userTable.setAge(31);
        userMapper.addUser(user);
        //报错
        //int i = 1/0;
        userTableMapper.addMsUser(userTable);
        return "OK";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String testMyAndMS2() {
        // 下面的语句报错，但是当前的事务管理器是默认的（MySQL事务管理器）
        // 发送报错时，MS的插入还没有执行，所有可以回滚成功
        User user = new User();
        user.setId(new Date().getTime());
        user.setName("小红2");
        user.setAge(222);
        UserTable userTable = new UserTable();
        userTable.setName("小铁2");
        userTable.setAge(312);
        userMapper.addUser(user);
        //报错
        int i = 1 / 0;
        userTableMapper.addMsUser(userTable);
        return "OK";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String testMyAndMS3() {
        // 下面的语句报错，但是当前的事务管理器是默认的（MySQL事务管理器）
        // 发送报错时，MS的插入已经执行，MS无法回滚
        User user = new User();
        user.setId(new Date().getTime());
        user.setName("小红3");
        user.setAge(223);
        UserTable userTable = new UserTable();
        userTable.setName("小铁3");
        userTable.setAge(313);
        userTableMapper.addMsUser(userTable);
        //报错
        int i = 1 / 0;
        userMapper.addUser(user);

        return "OK";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String testMyAndMS4() {
        // 下面的语句报错，但是当前的事务管理器是默认的（MySQL事务管理器）
        // 发送报错时，MS的插入已经执行，所有只有MySQL回滚成功,MS无法回滚
        User user = new User();
        user.setId(new Date().getTime());
        user.setName("小红4");
        user.setAge(224);
        UserTable userTable = new UserTable();
        userTable.setName("小铁4");
        userTable.setAge(314);
        userMapper.addUser(user);

        userTableMapper.addMsUser(userTable);
        //报错
        int i = 1 / 0;
        return "OK";
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String testMyAndMS5() {
        // 下面的语句报错，但是当前的事务管理器是默认的（MySQL事务管理器）
        // 发送报错的位置在addTableUser方法内部，而addTableUser方法增加了MS事务管理器，所以MS可以回滚
        // 然后错误往上抛，testMyAndMS5()方法也触发异常，MySQL插入也回滚。
        // 此时两个数据库的插入在出现异常时，都可以回滚
        // 总结：不同的数据库操作，不要放在同一个方法内，否则无法回滚；建议同一个的数据库操作写一个方法，然后指定对应事务管理器
        User user = new User();
        user.setId(new Date().getTime());
        user.setName("小红6");
        user.setAge(226);
        UserTable userTable = new UserTable();
        userTable.setName("小铁6");
        userTable.setAge(316);
        userMapper.addUser(user);
        //报错
        this.addTableUser(userTable);
        return "OK";
    }

    public Integer addTableUser(UserTable userTable) {
        // 手动控制事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("myTransaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = sqlserverTransactionManager.getTransaction(def);

        Integer i1;
        try {
            i1 = userTableMapper.addMsUser(userTable);
            int i = 1 / 0;
            sqlserverTransactionManager.commit(status);
        } catch (Exception e) {
            sqlserverTransactionManager.rollback(status);
            // 再次把错误抛给外面
            throw e;
        }
        return i1;
    }

}
