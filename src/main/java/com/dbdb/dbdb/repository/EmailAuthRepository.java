package com.dbdb.dbdb.repository;

import com.dbdb.dbdb.dto.EmailAuthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}