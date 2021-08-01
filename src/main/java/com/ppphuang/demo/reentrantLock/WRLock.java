package com.ppphuang.demo.reentrantLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WRLock {
    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();

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
class DataContainer {
    private Object data;
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock r = rw.readLock();
    private ReentrantReadWriteLock.WriteLock w = rw.writeLock();

    public Object read() {
        log.debug("获取读锁");
        r.lock();
        try {
            log.debug("读取");
            Thread.sleep(1000);
            return data;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            log.debug("释放读锁");
            r.unlock();
        }
    }

    public Object write() {
        log.debug("获取写锁");
        w.lock();
        try {
            log.debug("写入");
            return data;
        } finally {
            log.debug("释放写锁");
            w.unlock();
        }
    }
}