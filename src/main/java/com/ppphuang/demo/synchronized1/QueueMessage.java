package com.ppphuang.demo.synchronized1;

import java.util.LinkedList;

public class QueueMessage {
    public static void main(String[] args) {
        Queue queue = new Queue(3);
        for (int i = 0; i < 5; i++) {
            int id = i;
            new Thread(() -> {
                queue.put(new Message(id,"值"+id));
            },"t" + i).start();
        }
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = queue.get();
                System.out.println(message);
            }

        },"tt").start();
    }
}

class Queue {
    private LinkedList<Message> list = new LinkedList<>();

    private int capacity;

    public Queue(int capacity) {
        this.capacity = capacity;
    }

    public Message get() {
        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //list.notifyAll();
            Message message = list.removeFirst();
            //这里调用 notifyAll 与上面调用效果一样 都是当前线程释放之后 被唤醒的线程与其他等待线程（Blocked）一起竞争锁
            //因为notifyAll是将wait进程由waitSet唤醒到entrySet中 然后当前线程释放锁以后entrySet中的线程一起竞争锁
            //JavaDoc: The awakened threads will not be able to proceed until the current thread relinquishes the lock on this object.
            list.notifyAll();
            return message;
        }
    }

    public void put(Message message) {
        synchronized (list) {
            while (list.size() == capacity) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.addLast(message);
            capacity++;
            list.notifyAll();
        }
    }
}

final class Message {
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}

