package com.ppphuang.demo.dao;

import jdk.nashorn.internal.objects.annotations.Getter;

public class CounterData {
    private static int counter = 0;
    public static int reset() {
        counter = 0;
        return counter;
    }
    public synchronized void wrong() {
        counter++;
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        CounterData.counter = counter;
    }
}
