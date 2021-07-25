package com.ppphuang.demo.jmm;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceTest {


    public static void main(String[] args) {
        String val = "a";
        AtomicStampedReference<String> ref = new AtomicStampedReference<>(val, 0);

        String pre = ref.getReference();
        int stamp = ref.getStamp();
        funA(ref);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //这里会失败，因为拿到的版本号不是最新的
        boolean b = ref.compareAndSet(pre, "b", stamp, stamp + 1);
        System.out.println(b);
    }

    public static void funA(AtomicStampedReference<String> ref) {
        String pre = ref.getReference();
        int stamp = ref.getStamp();
        boolean b = ref.compareAndSet(pre, "b", stamp, stamp + 1);
        System.out.println(b);
        funB(ref);
    }

    public static void funB(AtomicStampedReference<String> ref) {
        String pre = ref.getReference();
        int stamp = ref.getStamp();
        boolean b = ref.compareAndSet(pre, "a", stamp, stamp + 1);
        System.out.println(b);
    }
}


