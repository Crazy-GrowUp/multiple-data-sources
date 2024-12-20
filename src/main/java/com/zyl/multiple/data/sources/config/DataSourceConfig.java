package com.zyl.multiple.data.sources.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name="dataSourceMs")
    @ConfigurationProperties(prefix = "spring.datasource.sqlserver")
    public DataSource dataSourceMs(){
        return DataSourceBuilder.create().build();
    }
}
