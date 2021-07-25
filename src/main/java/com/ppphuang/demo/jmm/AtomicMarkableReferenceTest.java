package com.ppphuang.demo.jmm;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AtomicMarkableReferenceTest {
    public static void main(String[] args) {
        GarbageBag bag = new GarbageBag("装满了垃圾");
        AtomicMarkableReference<GarbageBag> ref = new AtomicMarkableReference<>(bag, true);
        System.out.println("start...");
        GarbageBag pre = ref.getReference();
        boolean marked = ref.isMarked();
        System.out.println(pre.toString());
        funcA(ref, bag);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("想换一个新垃圾袋？");
        boolean success = ref.compareAndSet(pre, new GarbageBag("空垃圾袋"), marked, !marked);
        System.out.println("换了吗？" + success);
        System.out.println(ref.getReference().toString());
    }

    public static void funcA(AtomicMarkableReference<GarbageBag> ref, GarbageBag bag) {
        boolean marked = ref.isMarked();
        bag.setDesc("空垃圾袋");
        ref.compareAndSet(bag, bag, marked, !marked);
        System.out.println(bag.toString());
    }
}

class GarbageBag {
    String desc;

    public GarbageBag(String str) {
        this.desc = str;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return super.toString() + " " + desc;
    }
}
