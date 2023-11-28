package com.dbdb.dbdb.domain.userlog.repository;

import com.dbdb.dbdb.domain.ticket.dto.TicketDto;
import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.domain.user.repository.UserRepository;
import com.dbdb.dbdb.domain.userlog.dto.UserlogDto;
import com.dbdb.dbdb.domain.userlog.dto.VisualizationUserlogDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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

    public String bikeReturn(int userId, String arrivalStation, LocalDateTime arrivalTime, int useDistance) {

        Integer bike_id, history_id, station_status;
        int max_stands = 20, cur_stands;

        try {
            station_status = jdbcTemplate.queryForObject(
                    "SELECT station_status FROM bikestationinformation WHERE lendplace_id = ?",
                    new Object[]{arrivalStation},
                    Integer.class
            );
        } catch (EmptyResultDataAccessException e) {
            station_status = 0;
        }

        log.info("station_status={}", station_status);

        if (station_status == 0 || station_status == null)
            return "FAILED_INVALID_RETURN_STATION";

        String sql = "SELECT COUNT(*) FROM bike WHERE lendplace_id = ?";
        cur_stands = jdbcTemplate.queryForObject(sql, new Object[]{arrivalStation}, Integer.class);

        if (cur_stands >= max_stands)
            return "FAILED_OVER_MAX_STANDS";

        LocalDateTime departure_time;

        try {
            departure_time = jdbcTemplate.queryForObject(
                    "SELECT ul.departure_time FROM userlog ul " +
                            "WHERE ul.user_id = ? AND ul.return_status = 0 " +
                            "ORDER BY ul.departure_time DESC LIMIT 1",
                    new Object[]{userId},
                    LocalDateTime.class
            );
        } catch (EmptyResultDataAccessException e) {
            departure_time = null;
        }

        LocalDateTime arrival_time = arrivalTime;
        Duration duration = Duration.between(departure_time, arrival_time);
        long use_time = duration.getSeconds() / 60;

        // userlog table에서 user_id에 해당하는 유저의 return_status가 0인 bike_id를 반환하여 bike_id 지역 변수에 저장하기

        try {
            bike_id = jdbcTemplate.queryForObject(
                    "SELECT ul.bike_id FROM userlog ul " +
                            "WHERE ul.user_id = ? AND ul.return_status = 0 " +
                            "ORDER BY ul.departure_time DESC LIMIT 1",
                    new Object[]{userId},
                    Integer.class
            );
        } catch (EmptyResultDataAccessException e) {
            bike_id = null;
        }
        // userlog table에서 매개변수 userId와 user_id가 같고, 매개변수 bike_id와 bike_id가 같고,
        // return_status가 0인 컬럼의 arrival_station에 매개변수 arrivalStation, arrival_time에 매개변수 arrival_time,use_time에 매개변수 useTime,
        // use_distance에 매개변수 useDistance, return_status는 0인 경우 1로 바꾸기
        jdbcTemplate.update(
                "UPDATE userlog SET arrival_station = ?, arrival_time = ?, use_time = ?, use_distance = ?, return_status = 1 " +
                        "WHERE user_id = ? AND bike_id = ? AND return_status = 0",
                arrivalStation, arrivalTime, use_time, useDistance, userId, bike_id
        );

        // 지역 변수 bike_id와 일치하는 bike table의 id에 해당하는 컬럼의 use_status를 1에서 0으로 바꾸고, bike table의 lendplaced_id를 매개변수인 arrivalStation으로 바꾸기
        jdbcTemplate.update(
                "UPDATE bike SET use_status = 0, lendplace_id = ? WHERE id = ?",
                arrivalStation, bike_id
        );


        return "SUCCESS_BIKE_RETURN";

    }

    public List<UserlogDto> getUserlog(int userId) {
        String sql = "SELECT * FROM userlog WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new UserlogRowMapper());
    }

    public List<VisualizationUserlogDto.userUseTimeInfo> getTopUseTime() {
        var rowMapper = BeanPropertyRowMapper.newInstance(VisualizationUserlogDto.userUseTimeInfo.class);
        return jdbcTemplate.query("SELECT U.*, SUM(UL.use_time) AS total_use_time " +
                "FROM userlog UL " +
                "JOIN user U ON UL.user_id = U.id " +
                "WHERE UL.user_id IS NOT NULL " +
                "GROUP BY U.id " +
                "ORDER BY total_use_time DESC; ",
                rowMapper
        );
    }

    public List<VisualizationUserlogDto.userUseCountInfo> getTopUseCount() {
        var rowMapper = BeanPropertyRowMapper.newInstance(VisualizationUserlogDto.userUseCountInfo.class);
        return jdbcTemplate.query("SELECT U.*, COUNT(UL.user_id) AS total_use_count " +
                "FROM user U LEFT JOIN userlog UL ON U.id = UL.user_id " +
                "WHERE UL.user_id IS NOT NULL " +
                "GROUP BY U.id " +
                "ORDER BY total_use_count DESC;",
                rowMapper
        );
    }

    public List<VisualizationUserlogDto.userUseDistanceInfo> getTopUseDistance() {
        var rowMapper = BeanPropertyRowMapper.newInstance(VisualizationUserlogDto.userUseDistanceInfo.class);
        return jdbcTemplate.query("SELECT U.*, SUM(UL.use_distance) AS total_use_distance " +
                        "FROM user U LEFT JOIN userlog UL ON U.id = UL.user_id " +
                        "WHERE UL.user_id IS NOT NULL " +
                        "GROUP BY U.id " +
                        "ORDER BY total_use_distance DESC;",
                rowMapper
        );
    }

    private static class UserlogRowMapper implements RowMapper<UserlogDto> {
        @Override
        public UserlogDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserlogDto userlogDto = new UserlogDto();
            userlogDto.setLog_id(rs.getInt("log_id"));
            userlogDto.setUser_id(rs.getInt("user_id"));
            userlogDto.setBike_id(rs.getInt("bike_id"));
            userlogDto.setHistory_id(rs.getInt("history_id"));
            userlogDto.setDeparture_station(rs.getString("departure_station"));
            userlogDto.setArrival_station(rs.getString("arrival_station"));
            userlogDto.setDeparture_time(rs.getTimestamp("departure_time").toLocalDateTime());
            userlogDto.setArrival_time(rs.getTimestamp("arrival_time").toLocalDateTime());
            userlogDto.setUse_time(rs.getInt("use_time"));
            userlogDto.setUse_distance(rs.getInt("use_distance"));
            userlogDto.setReturn_status(rs.getInt("return_status"));
            return userlogDto;
        }
    }
}
