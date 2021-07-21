package com.ppphuang.demo.synchronized1;


public class RepeatABC {
    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify(1, 5);
        new Thread(()->{waitNotify.print("a",1,2);}).start();
        new Thread(()->{waitNotify.print("b",2,3);}).start();
        new Thread(()->{waitNotify.print("c",3,1);}).start();
    }
}
class WaitNotify{
    private int flag;
    private int loopNum;

    public WaitNotify(int flag, int loopNum) {
        this.flag = flag;
        this.loopNum = loopNum;
    }

    public void print(String str, int waitFlg, int nextFlag) {
        for (int i = 0; i < loopNum; i++) {
            synchronized (this) {
                while (flag != waitFlg) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(str);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }
}