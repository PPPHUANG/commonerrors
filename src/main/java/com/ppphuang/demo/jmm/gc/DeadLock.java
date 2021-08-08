package com.ppphuang.demo.jmm.gc;

public class DeadLock {
    /**
     * JConsole查看死锁
     * 线程死锁等待演示
     */
    static class SynAddRunalbe implements Runnable {
        int a, b;

        public SynAddRunalbe(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            synchronized (Integer.valueOf(a)) {
                synchronized (Integer.valueOf(b)) {
                    System.out.println(a + b);
                }
            }
        }
    }

    //    造成死锁的根本原因是Integer.valueOf()方法出于减少对象创建次数和节省内存的考虑，
//    会对数值为-128～127之间的Integer对象进行缓存[2]，如果valueOf()方法传入的参数在这个范围之内，
//    就直接返回缓存中的对象。也就是说代码中尽管调用了200次Integer.valueOf()方法，
//    但一共只返回了两个不同的Integer对象。假如某个线程的两个synchronized块之间发生了一次线程切换，
//    那就会出现线程A在等待被线程B持有的Integer.valueOf(1)，线程B又在等待被线程A持有的Integer.valueOf(2)，
//    结果大家都跑不下去的情况。
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new SynAddRunalbe(1, 2)).start();
            new Thread(new SynAddRunalbe(2, 1)).start();
        }
    }
}
