package com.ppphuang.demo.completablefuture;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest2 {

    public static void main(String[] args) {
//        thenApplyExample();
//        thenApplyAsyncExample();
//        thenCombineExample();
//        thenCombineAsyncExample();
//        thenComposeExample();
//        thenComposeAsyncExample();
//        applyToEitherExample();
//        allOf();
        thenSupplyAsyncExample();
    }

    static void thenSupplyAsyncExample() {
        // `fn` will run during the call to `complete()` in the context of whichever thread has called `complete()`.
        // If `complete()` has already finished by the time `thenApply()` is called, `fn` will be run in the context of the thread calling `thenApply()`.
        String original = "Message";
        CompletableFuture cf = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync thread: " + Thread.currentThread().getName());
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
            return original;
        }).thenApply(r -> {
            System.out.println("thenApply thread: " + Thread.currentThread().getName());
            return r;
        });
        System.out.println(cf.join());
    }

    static void thenApplyExample() {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original).thenApply(s -> {
            //主线程中执行
            System.out.println("1" + Thread.currentThread().getName());
            return s.toUpperCase();
        }).thenCombine(CompletableFuture.completedFuture(original).thenApply(s -> {
            //主线程中执行
            System.out.println("2" + Thread.currentThread().getName());
            return s.toLowerCase();
        }), (s1, s2) -> s1 + s2);
        System.out.println(cf.getNow(null));
    }

    static void thenApplyAsyncExample() {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original).thenApplyAsync(s -> {
            //线程池中执行
            System.out.println("1" + Thread.currentThread().getName());
            return s.toUpperCase();
        }).thenCombine(CompletableFuture.completedFuture(original).thenApplyAsync(s -> {
            //线程池中执行
            System.out.println("2" + Thread.currentThread().getName());
            return s.toLowerCase();
        }), (s1, s2) -> s1 + s2);
        System.out.println(cf.join());
    }

    static void thenCombineExample() {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original).thenApply(s -> {
            System.out.println("1" + Thread.currentThread().getName());
            return s.toUpperCase();
        }).thenCombine(CompletableFuture.completedFuture(original).thenApply(s -> {
            System.out.println("2" + Thread.currentThread().getName());
            return s.toLowerCase();
        }), (s1, s2) -> {
            //主线程中执行
            System.out.println("bifunction" + Thread.currentThread().getName());
            return s1 + s2;
        });
        System.out.println(cf.getNow(null));
    }

    static void thenCombineAsyncExample() {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original).thenApply(s -> {
            System.out.println("1" + Thread.currentThread().getName());
            return s.toUpperCase();
        }).thenCombineAsync(CompletableFuture.completedFuture(original).thenApply(s -> {
            System.out.println("2" + Thread.currentThread().getName());
            return s.toLowerCase();
        }), (s1, s2) -> {
            //线程池中执行
            System.out.println("bifunction" + Thread.currentThread().getName());
            return s1 + s2;
        });
        System.out.println(cf.join());
    }

    static void thenComposeExample() {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original).thenApplyAsync(s -> {
            System.out.println("1" + Thread.currentThread().getName());
            return s.toUpperCase();
        }).thenCompose(a -> {
            //这里在主线程执行
            System.out.println("3" + Thread.currentThread().getName());
            return CompletableFuture.completedFuture(original).thenApplyAsync(s -> {
                System.out.println("2" + Thread.currentThread().getName());
                return a + s.toLowerCase();
            });
        });
        System.out.println(cf.join());
    }


    static void thenComposeAsyncExample() {
        String original = "Message";
        CompletableFuture cf = CompletableFuture.completedFuture(original).thenApplyAsync(s -> {
            System.out.println("1" + Thread.currentThread().getName());
            return s.toUpperCase();
        }).thenComposeAsync(a -> {
            //在线程池中执行
            System.out.println("3" + Thread.currentThread().getName());
            return CompletableFuture.completedFuture(original).thenApplyAsync(s -> {
                System.out.println("2" + Thread.currentThread().getName());
                return a + s.toLowerCase();
            });
        });
        System.out.println(cf.join());
    }

    static void applyToEitherExample() {
        //会取两个任务最先完成的任务，上个任务和这个任务同时进行，哪个先结束，先用哪个
        String original = "Message";
        CompletableFuture<Object> objectCompletableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return original;
        }).applyToEither(CompletableFuture.supplyAsync(() -> "Message1"), (s -> s));
        System.out.println(objectCompletableFuture.join());
    }

    static void allOf() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Message1");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Message2");
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "Message3");
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.allOf(future1, future2, future3).thenApply(v -> {
            String join1 = future1.join();
            String join2 = future2.join();
            String join3 = future3.join();
            return join1 + join2 + join3;
        });
        System.out.println(stringCompletableFuture.join());
    }
}
