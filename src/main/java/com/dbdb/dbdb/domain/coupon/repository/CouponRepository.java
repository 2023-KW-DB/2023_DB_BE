package com.dbdb.dbdb.domain.coupon.repository;

import com.dbdb.dbdb.domain.coupon.dto.CouponDto;
import com.dbdb.dbdb.domain.coupon.dto.CouponFullOuterJoinTicketDto;
import com.dbdb.dbdb.domain.paymenthistory.repository.PaymentHistoryRepository;
import com.dbdb.dbdb.domain.userlog.dto.UserlogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
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
        // LEFT OUTER JOIN, UNION, RIGHT OUTER JOIN, FULL OUTER JOIN(LEFT OUTER JOIN+RIGHT OUTER JOIN)
        String sql = "SELECT * FROM coupon LEFT OUTER JOIN ticket ON coupon.ticket_id = ticket.id " +
                "UNION " +
                "SELECT * FROM coupon RIGHT OUTER JOIN ticket ON coupon.ticket_id = ticket.id;";
        List<CouponFullOuterJoinTicketDto> coupons = jdbcTemplate.query(
                sql, BeanPropertyRowMapper.newInstance(CouponFullOuterJoinTicketDto.class));

        Iterator<CouponFullOuterJoinTicketDto> iterator = coupons.iterator();
        while (iterator.hasNext()) {
            CouponFullOuterJoinTicketDto coupon = iterator.next();
            if (coupon.getValue() == null) {
                iterator.remove();
            }
        }

        return coupons != null ? coupons : new ArrayList<>();
    }

    public Integer registrationCoupon(int userId, String value) {

        try {
            CouponDto couponDto = jdbcTemplate.queryForObject(
                    "SELECT * FROM coupon WHERE value = ?",
                    new Object[]{value},
                    new BeanPropertyRowMapper<>(CouponDto.class)
            );

            if (couponDto != null && couponDto.getIs_used() == 1) {
                return null; // 이미 사용한 쿠폰인 경우(is_used가 1인 경우)
            }

            jdbcTemplate.update("SET @userId = ?", userId);

            // 쿠폰을 사용했으므로 is_used를 1로 변경
            String sqlUpdate = "UPDATE coupon SET is_used = 1 WHERE value = ?";
            jdbcTemplate.update(sqlUpdate, value);

//            assert couponDto != null;
//            String insertHistory = String.format("INSERT INTO paymenthistory (user_id, ticket_id, is_used, registration_at) VALUES (%d, %d, 0, '%s')", userId, couponDto.getTicket_id(), LocalDateTime.now().toString());
//            jdbcTemplate.update(insertHistory);

            return couponDto.getTicket_id();
        } catch (EmptyResultDataAccessException e) {
            return -1; // 쿠폰의 value와 일치하는 쿠폰이 없는 경우
        }
    }
}
