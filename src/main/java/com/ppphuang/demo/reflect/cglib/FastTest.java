package com.ppphuang.demo.reflect.cglib;

import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;

public class FastTest {
    public static void main(String[] args) throws InvocationTargetException {
        FastClass fastClass = FastClass.create(PersonInterface.class);
        FastMethod sayHi = fastClass.getMethod("sayHi", new Class<?>[]{String.class});
        String res = (String) sayHi.invoke(new PersonService01(), new Object[]{"tom"});
        System.out.println(res);
    }
}

interface PersonInterface {
    String sayHi(String name);

    int lengthOfName(String name);
}

class PersonService01 implements PersonInterface {
    @Override
    public String sayHi(String name) {
        return "hello " + name;
    }

    @Override
    public int lengthOfName(String name) {
        return name.length();
    }
}