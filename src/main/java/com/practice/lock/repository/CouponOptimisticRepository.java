package com.practice.lock.repository;

import com.practice.lock.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponOptimisticRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findById(Long id);
}
