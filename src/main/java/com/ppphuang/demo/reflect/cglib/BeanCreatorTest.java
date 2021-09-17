package com.ppphuang.demo.reflect.cglib;

import org.springframework.cglib.beans.BeanGenerator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanCreatorTest {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BeanGenerator beanGenerator = new BeanGenerator();
        beanGenerator.addProperty("name", String.class);
        Object myBean = beanGenerator.create();
        Method setName = myBean.getClass().getMethod("setName", String.class);
        setName.invoke(myBean, "tom");
        Method getName = myBean.getClass().getMethod("getName");
        System.out.println(getName.invoke(myBean));
    }
}
