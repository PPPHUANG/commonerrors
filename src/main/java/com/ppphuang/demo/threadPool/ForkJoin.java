package com.ppphuang.demo.threadPool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoin {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
//        System.out.println(forkJoinPool.invoke(new MyTask(5)));
        //合理拆分任务
        System.out.println(forkJoinPool.invoke(new MyTask1(1, 5)));
    }
}

class MyTask1 extends RecursiveTask<Integer> {
    int begin;
    int end;

    public MyTask1(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return "MyTask1{" +
                "begin=" + begin +
                ", end=" + end +
                '}';
    }

    @Override
    protected Integer compute() {
        if (begin == end) {
            return begin;
        }
        if (end - begin == 1) {
            return end + begin;
        }
        int mid = (end + begin) / 2;

        MyTask1 myTask1 = new MyTask1(begin, mid);
        myTask1.fork();
        MyTask1 myTask2 = new MyTask1(mid + 1, end);
        myTask2.fork();
        int res = myTask1.join() + myTask2.join();
        return res;
    }
}

class MyTask extends RecursiveTask<Integer> {
    private int n;

    public MyTask(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if (n == 1) {
            return 1;
        }
        MyTask myTask = new MyTask(n - 1);
        //让一个线程去执行
        myTask.fork();
        int res = n + myTask.join();
        return res;
    }
}