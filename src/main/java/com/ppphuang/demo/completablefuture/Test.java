package com.ppphuang.demo.completablefuture;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Test {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> 6 / 0);
        CompletableFuture<Integer> handle = integerCompletableFuture.handle(new DefaultValueHandle<>(true, 9, "handle", "params"));
        CompletableFuture<Integer> integerCompletableFuture1 = CompletableFuture.supplyAsync(() -> 6 / 3);
        CompletableFuture<Integer> whenComplete = integerCompletableFuture1
                .whenComplete(
                        new LogErrorAction<>("whenComplete", "params"));
        Integer integer = handle.get();
        System.out.println(integer);
        System.out.println(whenComplete.join());
    }

}
