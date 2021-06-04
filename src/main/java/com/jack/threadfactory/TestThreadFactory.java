package com.jack.threadfactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 验证
 * @author liangchen
 * @date 2021/6/4
 */
public class TestThreadFactory {

    public static void main(String[] args) {
        ThreadFactory threadFactory1 = Executors.defaultThreadFactory();
        Thread thread1 = threadFactory1.newThread(() -> System.out.println(11));
        Thread thread2 = threadFactory1.newThread(() -> System.out.println(22));


        ThreadFactory threadFactory2 = Executors.defaultThreadFactory();
        Thread thread21 = threadFactory2.newThread(() -> System.out.println(11));
        Thread thread22 = threadFactory2.newThread(() -> System.out.println(22));

        System.out.println(thread1.getName() + ":" + thread1.getPriority());
        System.out.println(thread2.getName() + ":" + thread1.getPriority());
        System.out.println(thread21.getName() + ":" + thread1.getPriority());
        System.out.println(thread22.getName() + ":" + thread1.getPriority());


    }
}
