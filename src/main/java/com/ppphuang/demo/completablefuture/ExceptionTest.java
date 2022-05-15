package com.ppphuang.demo.completablefuture;

import java.util.concurrent.CompletableFuture;

public class ExceptionTest {
    public static void main(String[] args) {
        try {
            System.out.println("success:\t" + divide(6, 3).get());
            System.out.println("exception:\t" + divide(6, 0).get());
        } catch (Exception exception) {
            System.out.println("catch=" + exception.getMessage());
        }


//        try {
//            System.out.println("success:\t" + whenComplete(6, 3).get());
//            System.out.println("exception:\t" + whenComplete(6, 0).get());
//        } catch (Exception exception) {
//            System.out.println("catch====" + exception.getMessage());
//        }
//
//        try {
//            System.out.println("success:\t" + exceptionally(6, 3).get());
//            System.out.println("exception:\t" + exceptionally(6, 0).get());
//        } catch (Exception exception) {
//            System.out.println("catch====" + exception.getMessage());
//        }
    }

    //1、handle()
    public static CompletableFuture divide(int a, int b) {
        return CompletableFuture.supplyAsync(() -> a / b)
                .handle((result, ex) -> {
                    if (null != ex) {
                        System.out.println(ex.getMessage());
                        return 0;
                    } else {
                        return result;
                    }
                });
    }

    // 2. whenComplete()
    public static CompletableFuture whenComplete(int a, int b) {
        return CompletableFuture.supplyAsync(() -> a / b)
                .whenComplete((result, ex) -> {
                    if (null != ex) {
                        System.out.println("whenComplete error:\t" + ex.getMessage());
                    }
                });
    }

    //3. exceptionly()
    public static CompletableFuture exceptionally(int a, int b) {
        return CompletableFuture.supplyAsync(() -> a / b)
                .exceptionally(ex -> {
                    System.out.println("ex:\t" + ex.getMessage());
                    return 0;
                });
    }

}
