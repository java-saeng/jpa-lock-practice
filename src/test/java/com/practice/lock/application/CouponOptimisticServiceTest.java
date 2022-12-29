package com.practice.lock.application;

import com.practice.lock.application.CouponOptimisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CouponOptimisticServiceTest {

    @Autowired
    CouponOptimisticService couponOptimisticService;

    @BeforeEach
    void init() {
        couponOptimisticService.saveCoupon();
    }

    @Test
    void test_lock() throws InterruptedException {

        int executeNumber = 15; //요청 횟수

        //스레드 풀 개수 설정
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < executeNumber; i++) {
            executorService.execute(() -> {
                try {
                    couponOptimisticService.issueCoupon(1L);
                    successCount.getAndIncrement();
                    System.out.println("성공");
                } catch (ObjectOptimisticLockingFailureException oolfe) {
                    System.out.println("낙관적 락 발생");
                    failCount.getAndIncrement();
                } catch (Exception e) {
                    failCount.getAndIncrement();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        System.out.println("성공한 횟수 = " + successCount.get());
        System.out.println("실패한 횟수 = " + failCount.get());

        assertEquals(failCount.get() + successCount.get(), executeNumber);
    }
}