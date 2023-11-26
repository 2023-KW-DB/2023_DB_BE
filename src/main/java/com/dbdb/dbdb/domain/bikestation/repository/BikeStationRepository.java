package com.dbdb.dbdb.domain.bikestation.repository;

import com.dbdb.dbdb.domain.bikestation.dto.BikeStationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BikeStationRepository {
    private final JdbcTemplate jdbcTemplate;
    public void insert(BikeStationDto.BikeStationDetailDto bikeStation) {
        if(bikeStation.getMax_stands() == null || bikeStation.getStation_status() == null) {
            String sql = "INSERT INTO `bikestationinformation` (`lendplace_id`, `statn_addr1`, `statn_addr2`, `startn_lat`, `startn_lnt`";
             List<Object> params = new ArrayList<>(Arrays.asList(bikeStation.getLendplace_id(), bikeStation.getStatn_addr1(), bikeStation.getStatn_addr2(), bikeStation.getStartn_lat(), bikeStation.getStartn_lnt()));

            if (bikeStation.getMax_stands() != null) {
                sql += ", `max_stands`";
                params.add(bikeStation.getMax_stands());
            }
            if (bikeStation.getStation_status() != null) {
                sql += ", `station_status`";
                params.add(bikeStation.getStation_status());
            }

            sql += ") VALUES (";
            sql += String.join(", ", Collections.nCopies(params.size(), "?"));
            sql += ")";

            jdbcTemplate.update(sql, params.toArray());
        } else {
            jdbcTemplate.update("INSERT INTO `bikestationinformation` (`lendplace_id`, `statn_addr1`, `statn_addr2`, `startn_lat`, `startn_lnt`, `max_stands`, `station_status`) VALUES (?,?,?,?,?,?,?)"
                    , bikeStation.getLendplace_id(), bikeStation.getStatn_addr1(), bikeStation.getStatn_addr2(), bikeStation.getStartn_lat(), bikeStation.getStartn_lnt(), bikeStation.getMax_stands(), bikeStation.getStation_status());
        }
    }

    public void update(BikeStationDto.BikeStationDetailDto bikeStation) {
        jdbcTemplate.update("UPDATE `bikestationinformation` SET " +
                        "statn_addr1= ?, " +
                        "statn_addr2=?, " +
                        "startn_lat=?, " +
                        "startn_lnt=?, " +
                        "max_stands=?, " +
                        "station_status=? " +
                        "WHERE lendplace_id=?"
                , bikeStation.getStatn_addr1(), bikeStation.getStatn_addr2(), bikeStation.getStartn_lat(), bikeStation.getStartn_lnt(), bikeStation.getMax_stands(), bikeStation.getStation_status(), bikeStation.getLendplace_id());
    }

    public void delete(String lendplaceId) {
        jdbcTemplate.update("DELETE FROM `bikestationinformation` WHERE lendplace_id=?", lendplaceId);
    }

    public BikeStationDto.BikeStationWithBikeDto findDetailByLendplaceId(String lendplaceId) { //TODO
        var bikeMapper = BeanPropertyRowMapper.newInstance(BikeStationDto.BikeStationWithBikeDto.class);
        return jdbcTemplate.queryForObject(
                "WITH BikeCount AS (" +
                "    SELECT lendplace_id, COUNT(*) AS total_bikes, " +
                "    SUM(CASE WHEN use_status = 0 AND bike_status = 1 THEN 1 ELSE 0 END) AS usable_bikes " +
                "    FROM bike " +
                "    GROUP BY lendplace_id ) " +
                "SELECT BS.*, COALESCE(BC.total_bikes, 0) AS total_bikes, (BS.max_stands - COALESCE(BC.total_bikes, 0)) AS empty_stands, " +
                "COALESCE(BC.usable_bikes, 0) AS usable_bikes " +
                "FROM bikestationinformation BS " +
                "LEFT JOIN BikeCount BC ON BS.lendplace_id = BC.lendplace_id " +
                "WHERE BS.lendplace_id = ?", bikeMapper, lendplaceId
        );
    }

    public List<BikeStationDto.BikeStationDetailDto> findDetailByName(String name) {
        var bikeMapper = BeanPropertyRowMapper.newInstance(BikeStationDto.BikeStationDetailDto.class);
        String likePattern = "%" + name + "%";
        return jdbcTemplate.query(
                "SELECT * FROM bikestationinformation " +
                        "WHERE statn_addr1 LIKE ? OR statn_addr2 LIKE ?",
                bikeMapper, likePattern, likePattern
        );
    }

    public BikeStationDto.BikeStationStatus findStatusById(String lendplaceId) {
        var bikeMapper = BeanPropertyRowMapper.newInstance(BikeStationDto.BikeStationStatus.class);
        return jdbcTemplate.queryForObject(
                "SELECT BS.*, " +
                        "       CASE " +
                        "           WHEN BikeCount.available_bikes IS NULL THEN 0 " +
                        "           ELSE BikeCount.available_bikes " +
                        "       END AS usable_bikes " +
                        "FROM bikestationinformation BS " +
                        "LEFT JOIN ( " +
                        "    SELECT lendplace_id, COUNT(*) AS available_bikes " +
                        "    FROM bike " +
                        "    WHERE use_status = 0 AND bike_status = 1 " +
                        "    GROUP BY lendplace_id " +
                        ") AS BikeCount ON BS.lendplace_id = BikeCount.lendplace_id " +
                        "WHERE BS.lendplace_id = ?", bikeMapper, lendplaceId
        );
    }

    public void createBikeCountsView() {
        jdbcTemplate.execute(
                "CREATE VIEW IF NOT EXISTS BikeCounts AS " +
                        "SELECT lendplace_id, COUNT(*) AS total_bikes, " +
                        "COUNT(CASE WHEN use_status=0 AND bike_status=1 THEN 1 END) AS usable_bikes " +
                        "FROM bike " +
                        "GROUP BY lendplace_id");
    }

    public List<BikeStationDto.BikeStationWithCurrentBike> findAll() {
        var bikeMapper = BeanPropertyRowMapper.newInstance(BikeStationDto.BikeStationWithCurrentBike.class);
        return jdbcTemplate.query(
                "SELECT BS.*, " +
                "       COALESCE(BC.total_bikes, 0) AS total_bikes, " +
                "       COALESCE(BC.usable_bikes, 0) AS usable_bikes, " +
                "       COALESCE(AVG(BR.rating), 0) AS average_rating " +
                "FROM bikestationinformation BS " +
                "LEFT JOIN BikeCounts BC ON BS.lendplace_id = BC.lendplace_id " +
                "LEFT JOIN bikestationrating BR ON BS.lendplace_id = BR.lendplace_id " +
                "GROUP BY BS.lendplace_id",
                bikeMapper);
    }

    public List<BikeStationDto.BikeStationSimpleWithState> findRecentByUserId(int userId) {
        var stationMapper = BeanPropertyRowMapper.newInstance(BikeStationDto.BikeStationSimpleWithState.class);
        return jdbcTemplate.query(
                "SELECT DISTINCT BSI.lendplace_id, BSI.statn_addr1, BSI.statn_addr2, UL.time " +
                        "FROM (" +
                        "    SELECT departure_station AS station, departure_time AS time " +
                        "    FROM userlog " +
                        "    WHERE user_id = ? AND departure_time IS NOT NULL " +
                        "    UNION ALL " +
                        "    SELECT arrival_station AS station, arrival_time AS time " +
                        "    FROM userlog " +
                        "    WHERE user_id = ? AND arrival_time IS NOT NULL " +
                        ") AS UL " +
                        "JOIN bikestationinformation BSI ON BSI.lendplace_id = UL.station " +
                        "ORDER BY UL.time DESC " +
                        "LIMIT 2",
                stationMapper,
                userId,
                userId
        );
    }

    public List<BikeStationDto.BikeStationSimpleState> findPopular() {
        var stationMapper = BeanPropertyRowMapper.newInstance(BikeStationDto.BikeStationSimpleState.class);
        return jdbcTemplate.query(
                "SELECT BSI.lendplace_id, BSI.statn_addr1, BSI.statn_addr2 " +
                        "FROM (" +
                        "    SELECT lendplace_id, AVG(rating) AS average_rating " +
                        "    FROM bikestationrating " +
                        "    GROUP BY lendplace_id " +
                        "    ORDER BY average_rating DESC " +
                        "    LIMIT 2" +
                        ") AS BR " +
                        "LEFT JOIN bikestationinformation BSI ON BR.lendplace_id = BSI.lendplace_id",
                stationMapper);
    }
}
