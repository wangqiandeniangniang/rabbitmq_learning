package com.jack.blockingQueue;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author liangchen
 * @date 2021/6/10
 */
public class LinkedBlockingQueueTest {
    public static void main(String[] args) {
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        LinkedBlockingQueue q = new LinkedBlockingQueue(integers);
        Spliterator spliterator = q.spliterator().trySplit();

    }
}
