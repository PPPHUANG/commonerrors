package com.ppphuang.demo.reflect.cglib;


import org.springframework.cglib.proxy.Mixin;

public class MiXinTest {
    public static void main(String[] args) {
        Mixin mixin = Mixin.create(new Class<?>[]{FirstInterface.class, SecondInterface.class, MixInterface.class}, new Object[]{new FirstImpl(), new SecondImpl()});
        MixInterface mixInterface = (MixInterface) mixin;
        System.out.println(mixInterface.getFirst());
        System.out.println(mixInterface.getSecond());
    }
}

interface FirstInterface {
    String getFirst();
}

class FirstImpl implements FirstInterface {
    @Override
    public String getFirst() {
        return "first behavior";
    }
}

interface SecondInterface {
    String getSecond();
}

class SecondImpl implements SecondInterface {
    @Override
    public String getSecond() {
        return "second behavior";
    }
}

interface MixInterface extends FirstInterface, SecondInterface {
}