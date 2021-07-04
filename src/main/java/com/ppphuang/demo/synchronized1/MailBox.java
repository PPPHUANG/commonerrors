package com.ppphuang.demo.synchronized1;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class MailBox {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        MailBoxes.getIds().forEach(id -> {
            new PostMan(id, "内容" + id).start();
        });
    }
}

class People extends Thread{
    @Override
    public void run() {
        GuardedObjectMail guardedObjectMail = MailBoxes.createGuardedObjectMail();
        System.out.println("收信ID：" + guardedObjectMail.getId());
        Object mail = guardedObjectMail.get(5000);
        System.out.println("收信ID：" + guardedObjectMail.getId() + "内容：" + mail);
    }
}

class PostMan extends Thread{
    private int id;
    private String mail;

    public PostMan(int id,String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObjectMail guardedObjectMail = MailBoxes.getGuardedObjectMail(id);
        System.out.println("送信ID：" + id + "内容：" + mail);
        guardedObjectMail.complete(mail);
    }
}

class MailBoxes{
    private static Map<Integer,GuardedObjectMail> boxes = new Hashtable<>();

    private static int id;

    private static synchronized int generateId() {
        return id++;
    }

    public static GuardedObjectMail getGuardedObjectMail(int id) {
           return  boxes.remove(id);
    }
    public static GuardedObjectMail createGuardedObjectMail() {
        GuardedObjectMail guardedObjectMail = new GuardedObjectMail(generateId());
        boxes.put(guardedObjectMail.getId(),guardedObjectMail);
        return guardedObjectMail;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}
class GuardedObjectMail {

    public GuardedObjectMail(int id) {
        this.id = id;
    }

    private int id;

    private Object response;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object get() {
        synchronized (this) {
            while (response == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    public Object get(long timeout) {
        long startTime = System.currentTimeMillis();
        long passedTime = 0;
        synchronized (this) {
            while (response == null) {
                long waitTime = timeout - passedTime;
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passedTime = System.currentTimeMillis() - startTime;
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}