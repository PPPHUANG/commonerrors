package com.ppphuang.demo.system.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 存在SqlSessionFactoryBean类型时，才会触发下面bean的装载
 */
@ConditionalOnClass({SqlSessionFactoryBean.class})
@Configuration
public class MysqlAutoConfiguration {
    @Bean
    public SqlSessionFactoryBeanPostProcessor sqlSessionFactoryBeanPostProcessor() {
        return new SqlSessionFactoryBeanPostProcessor();
    }
}
