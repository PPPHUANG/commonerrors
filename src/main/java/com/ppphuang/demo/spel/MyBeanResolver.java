package com.ppphuang.demo.spel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;

public class MyBeanResolver implements BeanResolver {
    private static ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");

    @Override
    public Object resolve(EvaluationContext evaluationContext, String beanName) throws AccessException {
        return appContext.getBean(beanName);
    }
}
