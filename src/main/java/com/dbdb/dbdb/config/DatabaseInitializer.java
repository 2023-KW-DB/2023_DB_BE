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
    public void run(String... args) throws Exception {
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
