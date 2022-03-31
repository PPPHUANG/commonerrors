package com.ppphuang.demo.reflect.javassist.serverproxy;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public String sayHello(String name, Integer age) {
        return name + age;
    }

    @Override
    public String sayHello(String name) {
        return name;
    }

    @Override
    public int sayHelloInt(Integer age) {
        return age;
    }

    @Override
    public Integer sayHelloInt(Integer age, long hight) {
        return Math.toIntExact(age + hight);
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

    @Override
    public List<Integer> sayHelloList(Integer age) {
        return Arrays.asList(1, 2, 3, 4, 5);
    }

    @Override
    public Result<Person> sayHelloPersion(Integer age, String name) {
        Person person = new Person(age, name);
        Result<Person> result = new Result<>(200, "成功", person);
        return result;
    }

    @Override
    public Result<List<Person>> sayHelloPersions(Integer age, String name) {
        Person person = new Person(age, name);
        Person person1 = new Person(age, name);
        Result<List<Person>> result = new Result<>(200, "成功", Arrays.asList(person, person1));
        return result;
    }
}
