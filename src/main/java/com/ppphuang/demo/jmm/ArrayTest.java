package com.ppphuang.demo.jmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ArrayTest {
    public static void main(String[] args) {
        demo(
                () -> new AtomicIntegerArray(10),
                AtomicIntegerArray::length,
//                (arr,index) -> arr.getAndIncrement(index),
                AtomicIntegerArray::getAndIncrement,
                System.out::println
        );

        demo(
                () -> new int[10],
                (a) -> a.length,
                (a, i) -> a[i]++,
                (a) -> System.out.println(Arrays.toString(a))
        );
    }

    /**
     * 参数1，提供数组、可以是线程不安全数组或线程安全数组
     * 参数2，获取数组长度的方法
     * 参数3，自增方法，传 array, index两个参数   array为数组，index为数组元素每次自增的元素的下标
     * 参数4，打印数组的方法
     * supplier 提供者 无中生有 ()->结果
     * function 函数 一个参数一个结果 (参数)->结果 , BiFunction (参数1,参数2)->结果
     * consumer 消费者 一个参数没结果 (参数)->void, BiConsumer (参数1,参数2)->
     */
    private static <T> void demo(Supplier<T> arraySupplier, Function<T, Integer> lengthFun, BiConsumer<T, Integer> putConsumer, Consumer<T> printConsumer) {
        ArrayList<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get();
        Integer length = lengthFun.apply(array);
        for (int i = 0; i < length; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array, j % length);
                }
            }));
        }
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        printConsumer.accept(array);
    }
}
