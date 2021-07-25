package com.ppphuang.demo.jmm;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LongAdderTest {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            demo(() -> new AtomicLong(0), AtomicLong::incrementAndGet);
        }
        for (int i = 0; i < 5; i++) {
            demo(LongAdder::new, LongAdder::increment);
        }
    }

    private static <T> void demo(Supplier<T> adderSupplier, Consumer<T> action) {
        T adder = adderSupplier.get();
        ArrayList<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 500000; j++) {
                    action.accept(adder);
                }
            }));
        }
        long start = System.nanoTime();
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(adder + " cost:" + (end - start) / 1000_000);
    }
}

