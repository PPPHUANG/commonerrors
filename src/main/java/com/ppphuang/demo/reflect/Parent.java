package com.ppphuang.demo.reflect;

import java.util.concurrent.atomic.AtomicInteger;

public class Parent <T>{
    AtomicInteger updateCount = new AtomicInteger();

    private T value;

    @Override
    public String toString() {
        return String.format("value: %s updateCount: %d", value, updateCount.get());
    }

    public void setValue(T value) {
        this.value = value;
        updateCount.incrementAndGet();
        System.out.println("Parent.setValue called");
    }
}
