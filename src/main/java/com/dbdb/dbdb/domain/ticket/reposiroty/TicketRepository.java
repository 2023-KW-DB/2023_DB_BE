package com.dbdb.dbdb.domain.ticket.reposiroty;

import com.dbdb.dbdb.domain.ticket.dto.TicketDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<TicketDto> returnAllTickets() {
        String sql = "SELECT * FROM ticket";
        return jdbcTemplate.query(
                sql,
                BeanPropertyRowMapper.newInstance(TicketDto.class)
        );
    }

    @Transactional
    public boolean purchaseTicket(int userId, int ticketId) {
        // Scalar Subqueries
        String priceQuery = "SELECT ticket_price FROM ticket " +
                "WHERE id = (SELECT t.id FROM ticket t WHERE t.id = ?)";
        Integer ticketPrice = jdbcTemplate.queryForObject(
                priceQuery, new Object[]{ticketId}, Integer.class);

        // BETWEEN
        String checkUserBalance = "SELECT COUNT(*) FROM user " +
                "WHERE id = ? AND total_money BETWEEN ? AND 99999999";
        Integer validUser = jdbcTemplate.queryForObject(
                checkUserBalance, new Object[]{userId, ticketPrice}, Integer.class);

        if (validUser == null || validUser == 0)
            return false;

        String updateUser = "UPDATE user SET total_money = total_money - ? WHERE id = ?";
        jdbcTemplate.update(updateUser, ticketPrice, userId);

        String insertHistory = String.format("INSERT INTO paymenthistory (user_id, ticket_id, is_used, registration_at) VALUES (%d, %d, 0, '%s')", userId, ticketId, LocalDateTime.now().toString());
        jdbcTemplate.update(insertHistory);

        return true;
    }
}
