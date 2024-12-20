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