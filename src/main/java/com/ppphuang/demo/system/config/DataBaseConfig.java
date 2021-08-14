package com.ppphuang.demo.system.config;

import com.ppphuang.demo.system.config.interceptor.MybatisLimitInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@Configuration
@EnableTransactionManagement
public class DataBaseConfig {

    @Bean(name = "sessionFactoryTestDb")
    public SqlSessionFactoryBean sessionFactoryTestDb() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        HikariConfig dbConfig = new HikariConfig();
        dbConfig.setDriverClassName("com.mysql.jdbc.Driver");
        dbConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useLocalSessionState=true&tinyInt1isBit=false&serverTimezone=Asia/Shanghai");
        dbConfig.setUsername("root");
        dbConfig.setPassword("root");
        dbConfig.setIdleTimeout(Long.parseLong("300000"));
        dbConfig.setMinimumIdle(Integer.parseInt("1"));
        dbConfig.setMaximumPoolSize(30);
        dbConfig.addDataSourceProperty("cachePrepStmts", "true");
        dbConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        dbConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dbConfig.addDataSourceProperty("useLocalSessionState", "true");
        dbConfig.addDataSourceProperty("cacheServerConfiguration", "true");
        dbConfig.addDataSourceProperty("cacheResultSetMetadata", "true");
        dbConfig.addDataSourceProperty("maintainTimeStats", "false");
        dbConfig.addDataSourceProperty("useSSL", "false");
        sqlSessionFactoryBean.setDataSource(new HikariDataSource(dbConfig));
//        sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
//        sqlSessionFactoryBean.setPlugins(new Interceptor[]{ new MybatisLimitInterceptor()});
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        return sqlSessionFactoryBean;
    }

    @Bean
    public MapperScannerConfigurer TestDbScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.ppphuang.demo.dao.mappers.test");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sessionFactoryTestDb");
        return mapperScannerConfigurer;
    }
}

