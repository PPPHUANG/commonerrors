package com.ppphuang.demo.system.config;

import com.ppphuang.demo.system.config.interceptor.MybatisLimitInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class SqlSessionFactoryBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //所有bean初始化之后都会进入这个方法，这个时候需要滤出需要的类型，比如这次就只需要拿到SqlSessionFactory类型的对象对其设置拦截器就行了
        if (bean instanceof SqlSessionFactoryBean) {
            SqlSessionFactoryBean nowBean = (SqlSessionFactoryBean) bean;
            //设置拦截器 todo 未生效
            nowBean.setPlugins(new Interceptor[]{new MybatisLimitInterceptor()});
        }
        //完成后返回出去，可能直接进入容器，也可能会去执行其他的BeanPostProcessor
        return bean;
    }
}
