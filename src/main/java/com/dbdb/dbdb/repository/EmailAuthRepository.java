package com.dbdb.dbdb.repository;

import com.dbdb.dbdb.dto.EmailAuthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class EmailAuthRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean createAuthCode(EmailAuthDto emailAuthDto) {
        String sql = "INSERT INTO emailauth (user_id, auth_num, created_at) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, emailAuthDto.getUser_id(), emailAuthDto.getAuth_num(), emailAuthDto.getCreated_at());

        return true;
    }

    public Boolean deleteByEmail(Integer user_id) {
        if (user_id != null) {
            String sql = "DELETE FROM emailauth WHERE user_id = ?";
            jdbcTemplate.update(sql, user_id);
        }

        return true;
    }

    public List<EmailAuthDto> findAll() {
        String sql = "SELECT * FROM emailauth";
        return jdbcTemplate.query(sql, new RowMapper<EmailAuthDto>() {
            @Override
            public EmailAuthDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                EmailAuthDto emailAuthDto = new EmailAuthDto();
                emailAuthDto.setUser_id(rs.getInt("user_id"));
                emailAuthDto.setAuth_num(rs.getInt("auth_num"));
                emailAuthDto.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
                return emailAuthDto;
            }
        });
    }

    public void deleteByUserId(int userId) {
        String sql = "DELETE FROM emailauth WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    public EmailAuthDto findByUserId(Integer userId) {
        try {
            String sql = "SELECT * FROM emailauth WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new RowMapper<EmailAuthDto>() {
                @Override
                public EmailAuthDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                    EmailAuthDto emailAuthDto = new EmailAuthDto();
                    emailAuthDto.setUser_id(rs.getInt("user_id"));
                    emailAuthDto.setAuth_num(rs.getInt("auth_num"));
                    emailAuthDto.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
                    return emailAuthDto;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("No record found for user_id: " + userId);
        }
    }
}