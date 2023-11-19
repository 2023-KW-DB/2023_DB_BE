package com.dbdb.dbdb.domain.userlog.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Slf4j
@Repository
public class UserlogRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String bikeRental(int userId, String departureStation) {
        Integer bike_id, history_id, station_status;

        try {
            // INNER JOIN
            history_id = jdbcTemplate.queryForObject(
                    "SELECT MIN(ph.history_id) FROM paymenthistory ph " +
                            "INNER JOIN user u ON ph.user_id = u.id " +
                            "WHERE u.id = ? AND ph.is_used = 0",
                    new Object[]{userId},
                    Integer.class
            );
        } catch (EmptyResultDataAccessException e) {
            history_id = -1;
        }

        if (history_id == null || history_id == -1)
            return "FAILED_NO_VALID_TICKET";

        try {
            station_status = jdbcTemplate.queryForObject(
                    "SELECT station_status FROM bikestationinformation WHERE lendplace_id = ?",
                    new Object[]{departureStation},
                    Integer.class
            );
        } catch (EmptyResultDataAccessException e) {
            station_status = 0;
        }

        log.info("station_status={}", station_status);

        if (station_status == 0 || station_status == null)
            return "FAILED_INVALID_RENTAL_STATION";

        try {
            // NESTED QUERY
            bike_id = jdbcTemplate.queryForObject(
                    "SELECT id FROM (SELECT id FROM bike WHERE lendplace_id = ? AND bike_status = 1 AND use_status = 0) AS AvailableBikes " +
                            "ORDER BY id LIMIT 1",
                    new Object[]{departureStation},
                    Integer.class
            );
        } catch (EmptyResultDataAccessException e) {
            bike_id = null;
        }

        log.info("id={}", bike_id);
        if (bike_id == null)
            return "FAILED_INVALID_BIKE";

        // 자전거 상태 및 결제 이력 업데이트
        jdbcTemplate.update(
                "UPDATE bike SET use_status = 1 WHERE id = ?",
                bike_id
        );


        jdbcTemplate.update(
                "UPDATE paymenthistory SET is_used = 1 WHERE history_id = ?",
                history_id
        );

        // Userlog 테이블에 정보 삽입
        jdbcTemplate.update(
                "INSERT INTO userlog (user_id, bike_id, history_id, departure_station, departure_time, return_status) VALUES (?, ?, ?, ?, ?, 0)",
                userId, bike_id, history_id, departureStation, LocalDateTime.now()
        );

        return "SUCCESS_BIKE_RENTAL";
    }
}
