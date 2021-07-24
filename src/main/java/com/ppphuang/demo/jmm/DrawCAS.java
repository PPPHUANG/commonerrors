package com.ppphuang.demo.jmm;

import java.util.concurrent.atomic.AtomicInteger;

public class DrawCAS {

    public static void main(String[] args) {
        Account.demo(new AccountSafe(10000));
    }
}

class AccountSafe implements Account{

    AtomicInteger atomicInteger ;

    public AccountSafe(Integer balance){
        this.atomicInteger =  new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return atomicInteger.get();
    }

    @Override
    public void withdraw(Integer amount) {
        // 核心代码
        while (true){
            int pre = getBalance();
            int next = pre - amount;
            if (atomicInteger.compareAndSet(pre,next)){
                break;
            }
        }
        // 可以简化为下面的方法
        // balance.addAndGet(-1 * amount);
    }
}