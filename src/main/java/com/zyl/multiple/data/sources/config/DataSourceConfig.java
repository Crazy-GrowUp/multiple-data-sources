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
