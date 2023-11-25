package com.dbdb.dbdb.fcm;

import com.dbdb.dbdb.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FCMTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveToken(UserDto userDto) {
        String sql = "UPDATE user SET fcm_token = ? WHERE email = ?";
        jdbcTemplate.update(sql, userDto.getFcm_token(), userDto.getEmail());
    }

    public String getToken(String email) {
        String sql = "SELECT fcm_token FROM user WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{email}, String.class);
    }

    public void deleteToken(String email) {
        String sql = "UPDATE user SET fcm_token = NULL WHERE email = ?";
        jdbcTemplate.update(sql, email);
    }

    public boolean hasKey(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        return count > 0;
    }
}