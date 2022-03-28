package com.ppphuang.demo.reflect.javassist.serverproxy;

import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public String sayHello(String name, Integer age) {
        return name + age;
    }

    @Override
    public Integer sayHelloInteger(Integer age) {
        return age;
    }

    @Override
    public int sayHelloInt(int age) {
        return new Integer(age);
    }

    @Override
    public long sayHelloLong(long age) {
        return age;
    }

    @Override
    public boolean sayHelloBoo(boolean age) {
        return age;
    }

    @Override
    public char sayHelloChar(char age) {
        return age;
    }

    @Override
    public double sayHelloDouble(double age) {
        return age;
    }

    @Override
    public float sayHelloFloat(float age) {
        return age;
    }

    @Override
    public byte sayHelloByte(byte age) {
        return age;
    }

    @Override
    public short sayHelloShort(short age) {
        return age;
    }
}
