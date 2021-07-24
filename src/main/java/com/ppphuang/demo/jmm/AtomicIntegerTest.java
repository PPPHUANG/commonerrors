package com.ppphuang.demo.jmm;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class AtomicIntegerTest {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);
        int i = atomicInteger.incrementAndGet();
        System.out.println(i);
        int i1 = atomicInteger.getAndIncrement();
        System.out.println(i1);
        int i2 = atomicInteger.decrementAndGet();
        System.out.println(i2);
        int i3 = atomicInteger.getAndDecrement();
        System.out.println(i3);
        int i4 = atomicInteger.getAndAdd(1);
        System.out.println(i4);
        int i44 = atomicInteger.addAndGet(1);
        System.out.println(i44);
        int i5 = atomicInteger.getAndSet(1);
        System.out.println(i5);
        atomicInteger.set(1);

        int i6 = atomicInteger.getAndUpdate(x -> x * 10);
        System.out.println(i6);
        int i7 = atomicInteger.updateAndGet(x -> x * 10);
        System.out.println(i7);
        int i8 = updateAndGet(atomicInteger, x -> x * 10);
        System.out.println(i8);
    }

    public static int updateAndGet(AtomicInteger atomicInteger, IntUnaryOperator updateFunction) {
        while (true) {
            int pre = atomicInteger.get();
            int val = updateFunction.applyAsInt(pre);
            if(atomicInteger.compareAndSet(pre,val)) {
                return val;
            }
        }
    }
}
