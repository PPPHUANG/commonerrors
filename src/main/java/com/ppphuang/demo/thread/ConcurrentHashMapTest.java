package com.ppphuang.demo.thread;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class ConcurrentHashMapTest {
    private static ConcurrentHashMap<String, LongAdder> map = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        //todo 修改为多线程版本
        LongAdder longAdder = new LongAdder();
        LongAdder a1 = map.computeIfAbsent("a", (a) -> longAdder);
        a1.increment();
        a1.increment();
        a1.increment();
        System.out.println(a1);
    }
}
