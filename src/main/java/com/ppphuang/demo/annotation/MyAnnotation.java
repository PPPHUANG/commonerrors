package com.ppphuang.demo.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME) //在注解上标记 @Inherited 元注解可以实现注解的继承 但还是继承获得方法上的注解
@Inherited
public @interface MyAnnotation {
    String value();
}
