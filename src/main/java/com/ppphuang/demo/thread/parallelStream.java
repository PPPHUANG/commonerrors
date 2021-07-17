package com.ppphuang.demo.thread;

import java.util.Arrays;
import java.util.List;

public class parallelStream {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14);
        integers.stream().parallel().forEach(i->{
            System.out.println(Thread.currentThread().getName());
            System.out.println(i);
        });
        integers.parallelStream().forEach(i->{
            System.out.println(Thread.currentThread().getName() + "==");
            System.out.println(i + "==");
        });
    }
}
