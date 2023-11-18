package com.dbdb.dbdb.domain.coupon.reposiroty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TicketRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTicket(int ticket_price) {
        String sql = "INSERT INTO ticket (ticket_price) VALUES (?)";
        jdbcTemplate.update(sql, ticket_price);
    }

    public void modifyTicket(int id, int newTicketPrice) {
        String sql = "UPDATE ticket SET ticket_price = ? WHERE id = ?";
        jdbcTemplate.update(sql, newTicketPrice, id);
    }

    public void deleteTicket(int id) {
        String sql = "DELETE FROM ticket WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
