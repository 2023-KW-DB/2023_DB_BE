package com.dbdb.dbdb.domain.coupon.repository;

import com.dbdb.dbdb.domain.coupon.dto.CouponDto;
import com.dbdb.dbdb.domain.userlog.dto.UserlogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CouponRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createCoupon(CouponDto couponDto) {
    }

    public void modifyCoupon(CouponDto couponDto) {
    }

    public void deleteCoupon(CouponDto couponDto) {
    }

    public List<CouponDto> getAllCoupon() {
        String sql = "SELECT * FROM coupon";
        List<CouponDto> coupons = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(CouponDto.class));
        return coupons != null ? coupons : new ArrayList<>();
    }
}
