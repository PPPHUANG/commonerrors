package com.ppphuang.demo.synchronized1;

import java.util.Random;

public class ExerciseTransfer {
    public static void main(String[] args) throws InterruptedException {
        Account a = new Account(1000);
        Account b = new Account(1000);
        Thread t1 = new Thread(() ->{
            for (int i = 0; i<1000;i++){
                a.transfer(b,randomAmount());
            }
        },"t1");
        Thread t2 = new Thread(() ->{
            for (int i = 0; i<1000;i++){
                b.transfer(a,randomAmount());
            }
        },"t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(a.getMoney() + b.getMoney());
    }

    static Random random = new Random();

    public static int randomAmount() {
        return random.nextInt(100) + 1;
    }
}

class Account{
    private int money;
    public Account(int money) {
        this.money = money;
    }

    public int getMoney() {
        return this.money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void transfer(Account target, int amount) {
        synchronized (Account.class){
            if (this.money >= amount) {
                //放里面也行，外面没有对money的写操作
//                synchronized (Account.class){
                this.setMoney(this.getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
            }
        }
    }
}