package com.ppphuang.demo.reentrantLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Slf4j
public class AqsDiyLock {
    public static void main(String[] args) {
        Mylock mylock = new Mylock();
        new Thread(() -> {
            mylock.lock();
//            mylock.lock(); //不可重入锁
            try {
                log.debug("locking");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mylock.unlock();
            }
        }, "t1").start();
        new Thread(() -> {
            mylock.lock();
            try {
                log.debug("locking");
            } finally {
                mylock.unlock();
            }
        }, "t2").start();
    }
}

/**
 * 自定义不可重入锁
 */
class Mylock implements Lock {
    //独占锁
    class MySync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                //加上 并设置owner为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            //State 为volatile 之前的指令不会重排序
            setState(0);
            return true;
        }

        //是否持有独占锁
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newConditon() {
            return new ConditionObject();
        }
    }

    private MySync sync = new MySync();

    //加锁（不成功加入等待队列）
    @Override
    public void lock() {
        sync.acquire(1);
    }

    //可打断加锁
    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    //尝试加锁（一次）
    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    //带超时加锁
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    //解锁
    @Override
    public void unlock() {
        sync.release(1);
    }

    //创建条件变量
    @Override
    public Condition newCondition() {
        return sync.newConditon();
    }
}