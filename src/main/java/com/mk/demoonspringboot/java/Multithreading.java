package com.mk.demoonspringboot.java;

import java.util.concurrent.CompletableFuture;

/**
 * @author Mark
 * @date 2020/11/12
 */
public class Multithreading {

    public void useCompletableFuture() {
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task1 return");
            return "task1 result";
        });
        CompletableFuture<String> task2 = task1.thenApply((res1) -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task2 return");
            return "task2 result with " + res1;
        });
        CompletableFuture<Void> task3 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task3 return");
        });
        try {
            //allOf中任一异常不会影响其他线程执行完。除了task2通过thenApply依赖task1的结果，task1异常的话task2就不会执行
            CompletableFuture.allOf(task1, task2, task3).join();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //getNow不会管线程完成了没有，join后调getNow一定能拿到线程返回值，不调join的话如果线程还没返回的话就会拿到default参数值
        String res1 = task1.getNow("default 1");
        String res2 = task2.getNow("default 2");
        System.out.println("res1 is " + res1);
        System.out.println("res2 is " + res2);
    }
}
