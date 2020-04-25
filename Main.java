package com.igordyac.igor;

import com.igordyac.igor.lab1.l1.L1Application;
import com.igordyac.igor.lab1.l2.L2Application;
import com.igordyac.igor.lab1.l3.L3Application;

import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
//        runWithSemaphore();
        runWithPhaser();
//        runWithLatch();
//        runWithBarrier();
//        runWithExchanger();

    }

    public static void runWithPhaser() throws InterruptedException{
        Phaser phsr = new Phaser(1);
        int curPhase;
        new L1PH(phsr,1000,1500);
        new L2PH(phsr,2000,2500);
        new L3PH(phsr,3000,3500);
        curPhase = phsr.getPhase();
        phsr.arriveAndAwaitAdvance();
        System.out.println("Phase " + curPhase + " Complete");
        curPhase = phsr.getPhase();
        phsr.arriveAndAwaitAdvance();
        System.out.println("Phase " + curPhase + " Complete");
        curPhase = phsr.getPhase();
        phsr.arriveAndAwaitAdvance();
        System.out.println("Phase " + curPhase + " Complete");
        phsr.arriveAndDeregister();
    }

    static class L1PH extends Thread
    {
        Phaser ph;
        int sleep1,sleep2;
        L1PH(Phaser p,int s1,int s2)
        {
            ph = p;
            sleep1 = s1;
            sleep2 = s2;
            ph.register();
            new Thread(this).start();
        }
        @Override
        public void run()
        {
            try {
                System.out.println("Preparation for lab 1 executing");
                Thread.sleep(sleep1);
                System.out.println("Lab 1 is ready");
                ph.arriveAndAwaitAdvance();
                System.out.println("Start of lab 1 executing");
                new L1Application();
                ph.arriveAndAwaitAdvance();
                System.out.println("Ending of lab 1 executing");
                Thread.sleep(sleep2);
                ph.arriveAndDeregister();
                System.out.println("Lab 1 ended");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    static class L2PH extends Thread
    {
        Phaser ph;
        int sleep1,sleep2;
        L2PH(Phaser p,int s1,int s2)
        {
            ph = p;
            sleep1 = s1;
            sleep2 = s2;
            ph.register();
            new Thread(this).start();
        }
        @Override
        public void run()
        {
            try {
                System.out.println("Preparation for lab 2 executing");
                Thread.sleep(sleep1);
                System.out.println("Lab 2 is ready");
                ph.arriveAndAwaitAdvance();
                System.out.println("Start of lab 2 executing");
                new L2Application();
                ph.arriveAndAwaitAdvance();
                System.out.println("Ending of lab 2 executing");
                Thread.sleep(sleep2);
                ph.arriveAndDeregister();
                System.out.println("Lab 2 ended");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    static class L3PH extends Thread
    {
        Phaser ph;
        int sleep1,sleep2;
        L3PH(Phaser p,int s1,int s2)
        {
            ph = p;
            sleep1 = s1;
            sleep2 = s2;
            ph.register();
            new Thread(this).start();
        }
        @Override
        public void run()
        {
            try {
                System.out.println("Preparation for lab 3 executing");
                Thread.sleep(sleep1);
                System.out.println("Lab 3 is ready");
                ph.arriveAndAwaitAdvance();
                System.out.println("Start of lab 3 executing");
                new L3Application();
                ph.arriveAndAwaitAdvance();
                System.out.println("Ending of lab 3 executing");
                Thread.sleep(sleep2);
                ph.arriveAndDeregister();
                System.out.println("Lab 3 ended");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void runWithExchanger() throws InterruptedException {
        var inputExchanger = new Exchanger<String>();
        var resultExchanger = new Exchanger<>();

        var executorService = Executors.newCachedThreadPool();

        executorService.submit(syncedWith(new L1Application(), inputExchanger, resultExchanger));

        System.err.println(inputExchanger.exchange("input input text"));
        System.err.println("Result exchanged = " + resultExchanger.exchange("finish"));

    }

    private static Runnable syncedWith(L1Application runnable, Exchanger<String> inputExchanger,
                                       Exchanger<Object> resultExchanger) {
        return () -> {
            try {
                var input = inputExchanger.exchange("waiting for input");
                var res = runnable.run(input);
                resultExchanger.exchange(res);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    public static void runWithBarrier() {
        var barrier = new CyclicBarrier(10);

        var executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }

            executorService.submit(syncedWith(new L3Application(), barrier));
        }
    }

    private static Runnable syncedWith(Runnable runnable, CyclicBarrier barrier) {
        return () -> {
            try {
                System.err.println("waiting..");
                barrier.await();
                System.err.println("executing..");
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            runnable.run();
        };
    }

    public static void runWithLatch() {
        var latch = new CountDownLatch(3);
        var semaphore = new Semaphore(1);

        var appsToRun = List.of(
                syncedWith(syncedWith(new L1Application(), semaphore), latch),
                syncedWith(syncedWith(new L2Application(), semaphore), latch),
                syncedWith(new L3Application(), latch)
        );

        var executorService = Executors.newFixedThreadPool(appsToRun.size());
        appsToRun.forEach(executorService::submit);


        try {
            System.err.println("So now main thread is waiting for others to finish");
            latch.await();
            System.err.println("Continuing executing main thread!");

            executorService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void runWithSemaphore() {
        var sync = new Semaphore(1);
        var appsToRun = List.of(
                syncedWith(new L1Application(), sync),
                syncedWith(new L2Application(), sync),
                new L3Application()
        );
        runAtTheSameTime(appsToRun);
    }

    private static Runnable syncedWith(Runnable runnable, Semaphore semaphore) {
        return () -> {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            runnable.run();
            semaphore.release();
        };
    }

    private static Runnable syncedWith(Runnable runnable, CountDownLatch latch) {
        return () -> {
            runnable.run();
            latch.countDown();
        };
    }

    private static void runAtTheSameTime(List<Runnable> tasks) {
        if (tasks.isEmpty()) {
            return;
        }

        try {
            var executorService = Executors.newFixedThreadPool(tasks.size());
            tasks.forEach(executorService::submit);

            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException("tasks could not be finished", e);
        }
    }
}
