package com.ppphuang.demo.reflect.javassist.serverproxy;

import java.util.List;

public interface TestService {
    String sayHello(String name, Integer age);

    String sayHello(String name);

    int sayHelloInt(Integer age);

    Integer sayHelloInt(Integer age, long hight);

    char sayHelloChar(char age);

    double sayHelloDouble(double age);

    float sayHelloFloat(float age);

    byte sayHelloByte(byte age);

    short sayHelloShort(short age);

    List<Integer> sayHelloList(Integer age);

    Result<Person> sayHelloPersion(Integer age, String name);

    Result<List<Person>> sayHelloPersions(Integer age, String name);
}
