package com.dbdb.dbdb.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void run(String... args) {
        // 트리거 존재 여부 확인
        String checkTrigger = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TRIGGERS WHERE TRIGGER_SCHEMA = ? AND TRIGGER_NAME = ?";
        Integer count = jdbcTemplate.queryForObject(checkTrigger, new Object[]{"dbdb", "assertion_user"}, Integer.class);

        // 트리거가 존재하지 않으면 생성
        if (count == null || count == 0) {
            String createTrigger = "CREATE TRIGGER assertion_user " +
                    "BEFORE INSERT ON user " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "    IF NEW.user_type NOT IN (0, 1, 2) THEN " +
                    "        SIGNAL SQLSTATE '45000' " +
                    "        SET MESSAGE_TEXT = 'user_type은 0, 1 or 2이어야 합니다'; " +
                    "    END IF; " +
                    "END;";
            jdbcTemplate.execute(createTrigger);
        }
    }
}