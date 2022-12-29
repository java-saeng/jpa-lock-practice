package com.practice.lock.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int count;

    @Version
    private Integer version;

    public Coupon(final int count) {
        this.count = count;
    }

    public void issueCoupon() {
        if (count <= 0) {
            throw new IllegalArgumentException("수량 부족");
        }
        count -= 1;
    }

    public int getCount() {
        return count;
    }
}
