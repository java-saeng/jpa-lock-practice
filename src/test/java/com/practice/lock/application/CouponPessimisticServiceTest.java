package com.practice.lock.application;

import com.practice.lock.application.CouponPessimisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.PessimisticLockingFailureException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CouponPessimisticServiceTest {

    @Autowired
    CouponPessimisticService couponPessimisticService;

    @BeforeEach
    void init() {
        couponPessimisticService.saveCoupon();
    }

    @Test
    void test_lock() throws InterruptedException {

        int executeNumber = 15;

        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        final CountDownLatch countDownLatch = new CountDownLatch(executeNumber);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < executeNumber; i++) {
            executorService.execute(() -> {
                try {
                    couponPessimisticService.issueCoupon(1L);
                    successCount.getAndIncrement();
                    System.out.println("성공");
                } catch (PessimisticLockingFailureException iae) {
                    System.out.println("비관적 락 발생");
                    failCount.getAndIncrement();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
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