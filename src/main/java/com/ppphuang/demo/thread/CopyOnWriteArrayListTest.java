package com.ppphuang.demo.thread;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListTest {
    public static void main(String[] args) {
        CopyOnWriteArrayList<Object> objects = new CopyOnWriteArrayList<>();
        objects.add(1);
        objects.add(2);
        objects.add(3);
        Iterator<Object> iterator = objects.iterator();
//        Object[] elements = getArray();
//        int len = elements.length;
//        Object[] newElements = Arrays.copyOf(elements, len + 1);
//        newElements[len] = e;
//        setArray(newElements);
        new Thread(() -> {
            //读写分离的  remove之后 拿到的iterator是旧的
            objects.remove(0);
            System.out.println(objects);
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
