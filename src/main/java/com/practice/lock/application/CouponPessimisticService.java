package com.practice.lock.application;

import com.practice.lock.domain.Coupon;
import com.practice.lock.repository.CouponPessimisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponPessimisticService {

    @Autowired
    private CouponPessimisticRepository couponPessimisticRepository;

    @Transactional
    public void issueCoupon(long couponId) {
        final Coupon coupon = couponPessimisticRepository.findById(couponId).get();

        coupon.issueCoupon();
    }

    public Coupon getCoupon() {
        return couponPessimisticRepository.findById(1L).get();
    }

    public void saveCoupon() {
        couponPessimisticRepository.save(new Coupon(5));
    }

    public void clear() {
        couponPessimisticRepository.deleteAll();
    }
}
