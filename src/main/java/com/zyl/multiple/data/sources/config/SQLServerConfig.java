package com.zyl.multiple.data.sources.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "com.zyl.multiple.data.sources.mapper.ms",
        sqlSessionFactoryRef = "sqlserverSqlSessionFactory"
)
public class SQLServerConfig {

    @Bean(name = "sqlserverSqlSessionFactory")
    public SqlSessionFactory sqlserverSqlSessionFactory(@Qualifier("dataSourceMs") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/ms/*.xml"));
        return factory.getObject();
    }

    @Bean(name = "sqlserverSqlSessionTemplate")
    public SqlSessionTemplate sqlserverSqlSessionTemplate(@Qualifier("sqlserverSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "sqlserverTransactionManager")
    public DataSourceTransactionManager sqlserverTransactionManager(@Qualifier("dataSourceMs") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
