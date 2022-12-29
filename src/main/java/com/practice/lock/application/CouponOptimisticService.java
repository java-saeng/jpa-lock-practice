package com.practice.lock.application;

import com.practice.lock.domain.Coupon;
import com.practice.lock.repository.CouponOptimisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponOptimisticService {

    @Autowired
    private CouponOptimisticRepository couponOptimisticRepository;

    @Transactional
    public void issueCoupon(long couponId) {
        final Coupon coupon = couponOptimisticRepository.findById(couponId).get();

        coupon.issueCoupon();
    }

    public Coupon getCoupon() {
        return couponOptimisticRepository.findById(1L).get();
    }

    public void saveCoupon() {
        couponOptimisticRepository.save(new Coupon(5));
    }
}
