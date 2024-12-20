package com.zyl.multiple.data.sources.config;

import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: multiple-data-sources
 * @description:
 * @author: yl.zhan
 * @create: 2024-12-19 16:22
 **/
@Configuration
// 扫描com.zyl.multiple.data.sources.mapper.mysql，使用sqlSessionFactory数据源
//basePackages 重要，最好把两个数据库的mapper包分开，以免被一个数据源扫描了，后续的就无法获得到对应的数据源了
@MapperScan(
        basePackages = "com.zyl.multiple.data.sources.mapper.mysql",
        sqlSessionFactoryRef = "sqlSessionFactory"
)
public class MySqlConfig {
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/mysql/*.xml"));
        return factory.getObject();
    }

    @Bean(name = "mysqlTransactionManager")
//    @Primary //(重要)配置mysql为主要事务管理器
    public DataSourceTransactionManager mysqlTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
