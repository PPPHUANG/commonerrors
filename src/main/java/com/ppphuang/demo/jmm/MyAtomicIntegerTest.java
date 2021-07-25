package com.ppphuang.demo.jmm;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class MyAtomicIntegerTest {
    public static void main(String[] args) {
        Account.demo(new MyAtomicInteger(10000));
    }
}

/**
 * 使用Unsafe自行实现一个AtomicInteger
 */
class MyAtomicInteger implements Account {
    private volatile int value;
    private static long valueOffset;
    //static final Unsafe UNSAFE;
    static Unsafe UNSAFE;

    static {
        Field theUnsafe = null;
        try {
            theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        theUnsafe.setAccessible(true);
        try {
            UNSAFE = (Unsafe) theUnsafe.get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            valueOffset = UNSAFE.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public int getValue() {
        return value;
    }

    public void decrement(int amount) {
        while (true) {
            int pre = this.value;
            int next = pre - amount;
            if (UNSAFE.compareAndSwapInt(this, valueOffset, pre, next)) {
                break;
            }
        }
    }

    public MyAtomicInteger(int value) {
        this.value = value;
    }

    @Override
    public Integer getBalance() {
        return value;
    }

    @Override
    public void withdraw(Integer amount) {
        decrement(amount);
    }
}
