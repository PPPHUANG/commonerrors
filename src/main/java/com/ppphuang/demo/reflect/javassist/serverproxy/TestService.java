package com.ppphuang.demo.reflect.javassist.serverproxy;

public interface TestService {
    String sayHello(String name, Integer age);

    Integer sayHelloInteger(Integer age);

    int sayHelloInt(int age);

    long sayHelloLong(long age);

    boolean sayHelloBoo(boolean age);

    char sayHelloChar(char age);

    double sayHelloDouble(double age);

    float sayHelloFloat(float age);

    byte sayHelloByte(byte age);

    short sayHelloShort(short age);
}
