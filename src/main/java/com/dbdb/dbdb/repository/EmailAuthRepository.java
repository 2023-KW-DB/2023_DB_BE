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
        jdbcTemplate.update(sql, ); {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, emailAuthDto.getUser_id());
                ps.setInt(2, emailAuthDto.getAuth_num());
                // LocalDateTime을 java.sql.Timestamp로 변환
                ps.setTimestamp(3, Timestamp.valueOf(emailAuthDto.getCreated_at()));
            }
        });
    }
}