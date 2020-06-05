package com.peashoot.blog;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class MultiThreadTests {
    @Test
    public void testReentrantLock() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        // lock.unlock();
        Thread.sleep(1000);
        Thread t1 = new Thread(() -> {
            // lock.lock();
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " interrupted.");
        });
        t1.start();
        Thread.sleep(1000);
        t1.interrupt();
        Thread.sleep(1000000);
    }

    @Test
    public void testThreadLocal() throws InterruptedException {
        ThreadLocal<SimpleDateFormat> formatter = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                System.out.println("Thread Name= "+Thread.currentThread().getName()+" default Formatter = "+formatter.get().toPattern());
                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //formatter pattern is changed here by thread, but it won't reflect to other threads
                formatter.set(new SimpleDateFormat());

                System.out.println("Thread Name= "+Thread.currentThread().getName()+" formatter = "+formatter.get().toPattern());
            }, "" + i);
            Thread.sleep(new Random().nextInt(1000));
            t.start();
            Callable<Object> result = Executors.callable(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }
    @Test
    public void testExecutor() {
        BlockingQueue<Runnable> runnables = new ArrayBlockingQueue<Runnable>(5);
        ExecutorService executor = new ThreadPoolExecutor(1,1,1,
                TimeUnit.SECONDS, runnables, new ThreadPoolExecutor.CallerRunsPolicy());
        // AbortPolicy          直接抛出异常，阻止系统工作。
        // CallerRunsPolicy     只要线程池未关闭，该策略直接在调用者线程中运行当前被丢弃的任务。
        // DiscardOldestPolicy 丢弃最老的一个请求任务，也就是丢弃一个即将被执行的任务，并尝试再次提交当前任务。
        // DiscardPolicy        默默的丢弃无法处理的任务，不予任何处理。
        for (int i = 0; i < 10; i++) {
            //创建WorkerThread对象（WorkerThread类实现了Runnable 接口）
            Runnable worker = new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " Start. Time = " + new Date());
                    processCommand();
                    System.out.println(Thread.currentThread().getName() + " End. Time = " + new Date());
                }
                private void processCommand() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            //执行Runnable
            executor.execute(worker);
        }
        //终止线程池
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }
}
