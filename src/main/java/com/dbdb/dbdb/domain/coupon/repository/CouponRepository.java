package com.dbdb.dbdb.domain.coupon.repository;

import com.dbdb.dbdb.domain.coupon.dto.CouponDto;
import com.dbdb.dbdb.domain.coupon.dto.CouponFullOuterJoinTicketDto;
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
        String sql = "INSERT INTO coupon (value, is_used, ticket_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, couponDto.getValue(), couponDto.getIs_used(), couponDto.getTicket_id());
    }

    public void modifyCoupon(CouponDto couponDto) {
        String sql = "UPDATE coupon SET ticket_id = ? WHERE value = ?";
        jdbcTemplate.update(sql, couponDto.getTicket_id(), couponDto.getValue());
    }

    public void deleteCoupon(CouponDto couponDto) {
        String sql = "DELETE FROM coupon WHERE value = ?";
        jdbcTemplate.update(sql, couponDto.getValue());
    }

    public List<CouponFullOuterJoinTicketDto> getAllCoupon() {
        String sql = "SELECT * FROM coupon LEFT OUTER JOIN ticket ON coupon.ticket_id = ticket.id " +
                "UNION " +
                "SELECT * FROM coupon RIGHT OUTER JOIN ticket ON coupon.ticket_id = ticket.id;";
        List<CouponFullOuterJoinTicketDto> coupons = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(CouponFullOuterJoinTicketDto.class));
        return coupons != null ? coupons : new ArrayList<>();
    }
}
