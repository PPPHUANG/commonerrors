package com.ppphuang.demo.reflect;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FieldObject {
    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",111);
        map.put("name","222");
        for(Map.Entry<String,Object> v : map.entrySet()) {
            System.out.println(v.getKey());
            System.out.println(v.getValue());
        }
        handleRequest(map);
    }

    private static void handleRequest(Object input) {
        System.out.println(input.getClass());
        Class<?> aClass = input.getClass();
        for (Field field : aClass.getDeclaredFields()) {
            System.out.println(field);
            System.out.println(field.getType());
            System.out.println(field.getClass());
        }
        HashMap<Object,Object> input1 = (HashMap<Object,Object>) input;
        for(Map.Entry<Object,Object> v : input1.entrySet()) {
            System.out.println(v.getKey());
            System.out.println(v.getValue());
            System.out.println(v.getValue().getClass());
        }

        input1.forEach(
                (i,v) -> {
                    System.out.println(i);
                    System.out.println(i.getClass());
                    System.out.println(v);
                    System.out.println(v.getClass());
                }
        );
    }
}
