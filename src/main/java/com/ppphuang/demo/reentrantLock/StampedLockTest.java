package com.ppphuang.demo.reentrantLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock
 * 没有条件变量
 * 不可重入
 */
public class StampedLockTest {
    public static void main(String[] args) {
        DataContainerStamped dataContainer = new DataContainerStamped();

        new Thread(() -> {
            dataContainer.read();
        }, "T1").start();

        new Thread(() -> {
            dataContainer.write();
        }, "T2").start();

        new Thread(() -> {
            dataContainer.read();
        }, "T3").start();

    }
}

@Slf4j
class DataContainerStamped {
    private Object data;
    private StampedLock rw = new StampedLock();

    public Object read() {
        long stamp = rw.tryOptimisticRead();
        log.debug("获取stamp" + stamp);
        if (rw.validate(stamp)) {
            return data;
        }
        try {
            stamp = rw.readLock();
            log.debug("获取读锁" + stamp);
            Thread.sleep(1000);
            return data;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            log.debug("释放读锁");
            rw.unlockRead(stamp);
        }
    }

    public Object write() {
        long stamp = rw.writeLock();
        log.debug("获取写锁" + stamp);
        try {
            log.debug("写入stamp" + stamp);
            return data;
        } finally {
            log.debug("释放写锁" + stamp);
            rw.unlock(stamp);
        }
    }
}