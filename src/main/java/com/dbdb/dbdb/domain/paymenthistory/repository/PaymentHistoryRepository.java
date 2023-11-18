package com.dbdb.dbdb.domain.paymenthistory.repository;

import com.dbdb.dbdb.domain.paymenthistory.dto.PaymentHistoryDto;
import com.dbdb.dbdb.domain.ticket.dto.TicketDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentHistoryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 이용권 구매 이력 조회
    public List<PaymentHistoryDto> returnAllPaymentHistorys(int userId) {
        String sql = "SELECT * FROM paymenthistory WHERE user_id = ?";
        return jdbcTemplate.query(
                sql,
                new Object[]{userId},
                BeanPropertyRowMapper.newInstance(PaymentHistoryDto.class)
        );
    }
}
