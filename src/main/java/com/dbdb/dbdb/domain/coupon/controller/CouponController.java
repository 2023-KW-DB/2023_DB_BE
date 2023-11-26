package com.dbdb.dbdb.domain.coupon.controller;

import com.dbdb.dbdb.domain.coupon.dto.CouponDto;
import com.dbdb.dbdb.domain.coupon.dto.CouponFullOuterJoinTicketDto;
import com.dbdb.dbdb.domain.coupon.service.CouponService;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CouponController {

    @Autowired
    private CouponService couponService;

    // 쿠폰 추가
    @PostMapping("/create-coupon")
    public ResponseEntity<?> createCoupon(@RequestBody CouponDto couponDto){

        couponService.createCoupon(couponDto);
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_CREATE_COUPON, null));
    }

//    // 쿠폰 수정
//    @PatchMapping("/modify-coupon")
//    public ResponseEntity<?> modifyCoupon(@RequestBody CouponDto couponDto){
//
//        couponService.modifyCoupon(couponDto);
//        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_MODIFY_COUPON, null));
//    }
//
//    // 쿠폰 삭제
//    @DeleteMapping("/delete-coupon")
//    public ResponseEntity<?> deleteCoupon(@RequestBody CouponDto couponDto){
//
//        couponService.deleteCoupon(couponDto);
//        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_DELETE_COUPON, null));
//    }

    // 모든 쿠폰 조회
    @GetMapping("/get-all-coupon")
    public ResponseEntity<?> getAllCoupon(){
        List<CouponFullOuterJoinTicketDto> allCoupon = couponService.getAllCoupon();

        if(allCoupon == null || allCoupon.isEmpty())
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_ALL_COUPON_EMPTY, null));
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_ALL_COUPON, allCoupon));
    }


}
