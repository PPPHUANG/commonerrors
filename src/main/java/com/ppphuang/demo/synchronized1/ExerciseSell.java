package com.ppphuang.demo.synchronized1;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class ExerciseSell {
    public static void main(String[] args) throws InterruptedException {
        TicketWindow ticketWindow = new TicketWindow(1000);
        ArrayList<Thread> threads = new ArrayList<>();
        Vector<Integer> amountList = new Vector<>();
        for (int i = 0; i < 2000; i++) {
            Thread thread = new Thread(() -> {
                int amount = ticketWindow.sell(random(5));
                amountList.add(amount);
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads){
            thread.join();
        }
        System.out.println("余票：" + ticketWindow.getCount());
        System.out.println("卖票：" + amountList.stream().mapToInt(i->i).sum());
    }

    static Random random = new Random();

    public static int random(int amount) {
        return random.nextInt(amount) + 1;
    }
}

class TicketWindow{
    private int count;

    public TicketWindow(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public synchronized int sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }
}
