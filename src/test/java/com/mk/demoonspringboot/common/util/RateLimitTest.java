package com.mk.demoonspringboot.common.util;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RateLimitTest {

    public static void main(String[] args) throws InterruptedException {
        RateLimiter rateLimiter = RateLimiter.create(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        int nTasks = 10;
        //睡1秒攒5个令牌
        Thread.sleep(1000);
        CountDownLatch countDownLatch = new CountDownLatch(nTasks);
        for (int i = 0; i < nTasks; i++) {
            final int j = i;
            executorService.submit(() -> {
                rateLimiter.acquire(1);
                System.out.println("thread " + j + " get it at " + System.currentTimeMillis());
                countDownLatch.countDown();
            });
        }
        executorService.shutdown();
        countDownLatch.await();
    }
}
