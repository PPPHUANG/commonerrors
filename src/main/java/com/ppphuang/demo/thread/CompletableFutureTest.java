package com.ppphuang.demo.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " : CompletableFuture");
            return 1024;
        });
        System.out.println("whenComplete start");
        CompletableFuture<Integer> integerCompletableFuture1 = integerCompletableFuture.whenComplete((r, e) -> {
            System.out.println(r);
            System.out.println(e);
        });
        System.out.println("whenComplete ing");
        integerCompletableFuture1.get();
        System.out.println("whenComplete end");

//        CompletableFuture<Integer> integerCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
//            System.out.println(Thread.currentThread().getName() + " : CompletableFuture1");
//            int i = 10 / 0;
//            return 1024;
//        });
//        integerCompletableFuture1.whenComplete((r,e) -> {
//            System.out.println(r);
//            System.out.println(e);
//        }).get();
    }
}
