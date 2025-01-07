# multiple-data-sources

### Introduction
Springboot multi-data source configuration, connection, transaction management, using annotations to solve quickly; MySQL，SQLServer
CSDN blog post: https://blog.csdn.net/qq_45062700/article/details/144610108
#### Do not use data pool
#### Several points to pay attention to
1. When Spring Boot configures the url of database connection, it should be written as jdbc-url.
```java
For example, the error is:
java.lang.IllegalArgumentException: jdbcUrl is required with driverClassName.
Then there is a high probability that the url is not written as jdbc-url.
```
2. Mapper class is best placed under two packages separately, and there is no relationship between parent and child packages;
```java
For example, the error is:
org.apache.ibatis.binding.BindingException: Invalid bound statement (not found): com.zyl.multiple.data.sources.mapper.xx.XxxMapper.getXxUser
The high probability is that the package placed by the Mapper class is not ready. You can understand it carefully according to the following contents.
```
```java
├── com.zyl.multiple.data.sources.mapper
│   └── ms
│       └──  UserTableMapper.java
│   └── mysql
│       └──  UserMapper.java
```
```java
@MapperScan(
basePackages = "com.zyl.multiple.data.sources.mapper.mysql",
sqlSessionFactoryRef = "sqlSessionFactory"
)
public class MySqlConfig {
......
}
```
```java
@Configuration
@MapperScan(
basePackages = "com.zyl.multiple.data.sources.mapper.ms",
sqlSessionFactoryRef = "sqlserverSqlSessionFactory"
)
public class SQLServerConfig {
......
}
```
It can be found that the basePackages values of @MapperScan of the two configuration classes are not inclusive;
If one of them is com.zyl.multiple.data.sources.mapper and the other is com.zyl.multiple.data.sources.mapper.xx.
Then it is very likely that the data source of the latter will not be available and will be fully represented by the former.

---
#### Use data pool (Druid)
1. Import druid dependency
```xml
    <!--Alibaba data source-->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.1.10</version>
    </dependency>
```
2. modify [application.yml](src%2Fmain%2Fresources%2Fapplication.yml)
```yaml
spring:
    application:
      name:
        multiple-data-sources
    datasource:
      mysql:
        #      driver-class-name: com.mysql.jdbc.Driver #Can be removed, if it is a special version, you can import it yourself.
        # When using jdbc to connect to the database in # spring, the url should be written as jdbc-url.
        # When using druid data source, write it as url.
        url: jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useAffectedRows=true&allowMultiQueries=true
        username: root
        password: 123456
        type: com.alibaba.druid.pool.DruidDataSource
        initial-size: 5 
        min-idle: 5 
        max-active: 50 
      sqlserver:
        #      driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver 
        url: jdbc:sqlserver://localhost:1433;databaseName=Test2;encrypt=false;trustServerCertificate=false;
        username: sa
        password: 123456
        type: com.alibaba.druid.pool.DruidDataSource 
        initial-size: 5 
        min-idle: 5 
        max-active: 50 
```

3. Modify data source loading configuration java file [DataSourceConfig.java](src%2Fmain%2Fjava%2Fcom%2Fzyl%2Fmultiple%2Fdata%2Fsources%2Fconfig%2FDataSourceConfig.java)
```java
package com.zyl.multiple.data.sources.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "dataSourceMs")
    @ConfigurationProperties(prefix = "spring.datasource.sqlserver")
    public DataSource dataSourceMs() {
        return new DruidDataSource();
    }
}
```
It only takes these three steps to complete, is it very simple? Go and try it.

#### Multi-data source transaction management
In this paper, we discuss whether the data of two data sources will be rolled back after the method reports an error by operating two data sources at the same time.
[MySqlConfig.java](src%2Fmain%2Fjava%2Fcom%2Fzyl%2Fmultiple%2Fdata%2Fsources%2Fconfig%2FMySqlConfig.java)
```java
    @Bean(name = "mysqlTransactionManager")
    @Primary//(Important) Configure mysql as the primary (default) transaction manager.
    public DataSourceTransactionManager mysqlTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
```
2. Rollback all, using manual control transaction commit.
```java
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String testMyAndMS5() {
        //The following statement gives an error, but the current transaction manager is the default (MySQL transaction manager).
        //The location where the error is sent is inside the addTableUser method, and the addTableUser method adds the MS transaction manager, so the MS can roll back.
        //Then the error is thrown up, the testMyAndMS5 () method also triggers an exception, and the MySQL insert rolls back.
        //At this time, when the insertion of two databases is abnormal, both can be rolled back.
        //Summary: Don't put different database operations in the same method, otherwise you can't roll back; It is suggested that the same database operation write a method and then specify the corresponding transaction manager.
        User user = new User();
        user.setId(new Date().getTime());
        user.setName("小红6");
        user.setAge(226);
        UserTable userTable = new UserTable();
        userTable.setName("小铁6");
        userTable.setAge(316);
        userMapper.addUser(user);
        //Report an error
        this.addTableUser(userTable);
        return "OK";
    }

    public Integer addTableUser(UserTable userTable) {
        // Manual control transaction
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
            // Throw the mistake outside again.
            throw e;
        }
        return i1;
    }
```
2. Only the data of the transaction manager in @Transactional is rolled back.
```java
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String testMyAndMS2() {
        //The following statement gives an error, but the current transaction manager is the default (MySQL transaction manager).
        //When sending the error, the insertion of MS has not been executed, so all can be rolled back successfully.
        User user = new User();
        user.setId(new Date().getTime());
        user.setName("小红2");
        user.setAge(222);
        UserTable userTable = new UserTable();
        userTable.setName("小铁2");
        userTable.setAge(312);
        userMapper.addUser(user);
        //Report an error
        int i = 1 / 0;
        userTableMapper.addMsUser(userTable);
        return "OK";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String testMyAndMS3() {
        //The following statement gives an error, but the current transaction manager is the default (MySQL transaction manager).
        //When sending an error, the insertion of the MS has been executed, and the MS cannot roll back.
        User user = new User();
        user.setId(new Date().getTime());
        user.setName("小红3");
        user.setAge(223);
        UserTable userTable = new UserTable();
        userTable.setName("小铁3");
        userTable.setAge(313);
        userTableMapper.addMsUser(userTable);
        //Report an error
        int i = 1 / 0;
        userMapper.addUser(user);

        return "OK";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String testMyAndMS4() {
        //The following statement gives an error, but the current transaction manager is the default (MySQL transaction manager).
        //When sending the error, the insertion of MS has been executed, so only MySQL can roll back successfully, and MS cannot roll back.
        User user = new User();
        user.setId(new Date().getTime());
        user.setName("小红4");
        user.setAge(224);
        UserTable userTable = new UserTable();
        userTable.setName("小铁4");
        userTable.setAge(314);
        userMapper.addUser(user);

        userTableMapper.addMsUser(userTable);
        //Report an error
        int i = 1 / 0;
        return "OK";
    }
```

For multi-transaction management, there is also a Seata distributed transaction management framework, which includes AT mode, TCC mode, Saga mode and XA mode.
