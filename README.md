# multiple-data-sources
#### 介绍
springboot多数据源配置，连接，事务管理，使用注解快速解决；MySQL，SQLServer
CSDN 博客文章：https://blog.csdn.net/qq_45062700/article/details/144610108
#### 不使用数据池
还有更多的数据库可以
##### 需要注意的几个点
1. springboot配置数据库连接的url时，要写成jdbc-url
```java
比如报错为：
java.lang.IllegalArgumentException: jdbcUrl is required with driverClassName.
那么很大概率就是url没写成jdbc-url
```
2. Mapper类最好是分开放到两个包下面，不要有父子包的关系；
```java
比如报错为：
org.apache.ibatis.binding.BindingException: Invalid bound statement (not found): com.zyl.multiple.data.sources.mapper.xx.XxxMapper.getXxUser
大概率就是Mapper类放置的包没弄好，可以根据下面的内容，仔细理解
```
```java
├── com.zyl.multiple.data.sources.mapper
│   └── ms
│       └── UserTableMapper.java
│   └── mysql
│       └── UserMapper.java
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
可以发现两个配置类的@MapperScan的basePackages值是没有包含关系的；
如果其中一个是com.zyl.multiple.data.sources.mapper，一个是com.zyl.multiple.data.sources.mapper.xx
那么就很有可能后者的数据源就无法获取到了，会被前者全部代理了。

---
#### 使用数据池 (Druid)
1. 导入druid依赖
```xml
    <!--阿里巴巴数据源-->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid-spring-boot-starter</artifactId>
        <version>1.1.10</version>
    </dependency>
```
2. 修改[application.yml](src%2Fmain%2Fresources%2Fapplication.yml)
```yaml
spring:
    application:
      name:
        multiple-data-sources
    datasource:
      mysql:
        #      driver-class-name: com.mysql.jdbc.Driver #可以去除，如果是特殊版本，可以自己导入
        # spring中使用jdbc连接数据库时，url要写成jdbc-url
        # 使用druid数据源时，要写成url
        url: jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useAffectedRows=true&allowMultiQueries=true
        username: root
        password: 123456
        type: com.alibaba.druid.pool.DruidDataSource # 连接池类型
        initial-size: 5  # 初始连接数
        min-idle: 5  # 闲置连接数
        max-active: 50  # 最大连接数
      sqlserver:
        #      driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver #可以去除，如果是特殊版本，可以自己导入
        url: jdbc:sqlserver://localhost:1433;databaseName=Test2;encrypt=false;trustServerCertificate=false;
        username: sa
        password: 123456
        type: com.alibaba.druid.pool.DruidDataSource # 连接池类型
        initial-size: 5  # 初始连接数
        min-idle: 5  # 闲置连接数
        max-active: 50  # 最大连接数
```

3. 修改数据源加载配置java文件 [DataSourceConfig.java](src%2Fmain%2Fjava%2Fcom%2Fzyl%2Fmultiple%2Fdata%2Fsources%2Fconfig%2FDataSourceConfig.java)
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
就只需要这三步就完成了，是不是很简单；快去试试吧

