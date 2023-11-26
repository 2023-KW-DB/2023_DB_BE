package com.dbdb.dbdb.domain.coupon.service;

import com.dbdb.dbdb.domain.coupon.dto.CouponDto;
import com.dbdb.dbdb.domain.coupon.dto.CouponFullOuterJoinTicketDto;
import com.dbdb.dbdb.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public void createCoupon(CouponDto couponDto) {
        couponRepository.createCoupon(couponDto);
    }

    public void modifyCoupon(CouponDto couponDto) {
        couponRepository.modifyCoupon(couponDto);
    }
//
//    public void deleteCoupon(CouponDto couponDto) {
//        couponRepository.deleteCoupon(couponDto);
//    }

    public List<CouponFullOuterJoinTicketDto> getAllCoupon() {
        return couponRepository.getAllCoupon();
    }
}
