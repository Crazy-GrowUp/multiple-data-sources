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


