package com.ppphuang.demo.system.config;


import com.github.pagehelper.PageHelper;
import com.ppphuang.demo.system.config.interceptor.EmojiTransferInterceptior;
import com.ppphuang.demo.system.config.interceptor.MybatisLimitInterceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 存在SqlSessionFactoryBean类型时，才会触发下面bean的装载
 */
@ConditionalOnClass({SqlSessionFactoryBean.class})
@Configuration
public class MysqlAutoConfiguration {
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("dialect", "mysql");
        pageHelper.setProperties(p);
        return pageHelper;
    }

    @Bean
    public MybatisLimitInterceptor mybatisLimitInterceptor() {
        return new MybatisLimitInterceptor();
    }

    @Bean
    public EmojiTransferInterceptior emojiTransferInterceptior() {
        return new EmojiTransferInterceptior();
    }


    @Bean
    public SqlSessionFactoryBeanPostProcessor sqlSessionFactoryBeanPostProcessor(PageHelper pageHelper, MybatisLimitInterceptor mybatisLimitInterceptor, EmojiTransferInterceptior emojiTransferInterceptior) {
        return new SqlSessionFactoryBeanPostProcessor(pageHelper, mybatisLimitInterceptor, emojiTransferInterceptior);
    }
}
