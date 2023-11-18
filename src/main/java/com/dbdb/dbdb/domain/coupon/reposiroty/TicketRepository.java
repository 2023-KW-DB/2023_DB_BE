package com.dbdb.dbdb.domain.coupon.reposiroty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TicketRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTicket(int ticket_price) {
        String sql = "INSERT INTO Ticket (ticket_price) VALUES (?)";
        jdbcTemplate.update(sql, ticket_price);
    }
}
