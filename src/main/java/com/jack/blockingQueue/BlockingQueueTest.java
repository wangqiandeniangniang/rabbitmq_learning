package com.jack.blockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 @author liangchen
 @date 2021/6/9
 */
public class BlockingQueueTest {
    public static void main(String[] args) {
        BlockingQueue q = new ArrayBlockingQueue(10);
        Producer p = new Producer(q);
        Consumer c1 = new Consumer(q);
        Consumer c2 = new Consumer(q);
        new Thread(p).start();
        new Thread(c1).start();
        new Thread(c2).start();
    }
}

class Producer implements Runnable {
    private final BlockingQueue queue;

    Producer(BlockingQueue q) {
        queue = q;
    }

    public void run() {
        try {
            while (true) {
                queue.put(produce());
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    Object produce() {

        Object o = new Object();
        System.out.println("produce ====" + o);
        return o;
    }
}

class Consumer implements Runnable {
    private final BlockingQueue queue;

    Consumer(BlockingQueue q) {
        queue = q;
    }

    public void run() {
        try {
            while (true) {
                consume(queue.take());
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    void consume(Object x) {
        System.out.println("consume===" + x);
    }
}

