package com.dbdb.dbdb.domain.bikestation.repository;

import com.dbdb.dbdb.domain.bikestation.dto.BikeStationDto;
import com.dbdb.dbdb.domain.bikestationrating.dto.BikeStationRatingDto;
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
                // WITH, SUM
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
                // STRING OPERATION(likePattern), LIKE
                "WITH BikeCount AS (" +
                        "    SELECT lendplace_id, COUNT(*) AS total_bikes, " +
                        "    SUM(CASE WHEN use_status = 0 AND bike_status = 1 THEN 1 ELSE 0 END) AS usable_bikes " +
                        "    FROM bike " +
                        "    GROUP BY lendplace_id ) " +
                "SELECT BSI.*, COALESCE(AVG(BSR.rating), 0) AS average_rating, COALESCE(BC.usable_bikes, 0) AS usable_bikes " + // 평균 평점을 계산합니다.
                        "FROM bikestationinformation BSI " +
                        "LEFT JOIN bikestationrating BSR ON BSI.lendplace_id = BSR.lendplace_id " + // bikestationrating과 조인합니다.
                        "LEFT JOIN BikeCount BC ON BSI.lendplace_id = BC.lendplace_id " +
                        "WHERE BSI.statn_addr1 LIKE ? OR BSI.statn_addr2 LIKE ? " +
                        "GROUP BY BSI.lendplace_id", // lendplace_id에 대해 그룹화합니다.
                bikeMapper, likePattern, likePattern
        );
    }

    public BikeStationDto.BikeStationStatus findStatusById(String lendplaceId, int userId) {
        var bikeMapper = BeanPropertyRowMapper.newInstance(BikeStationDto.BikeStationStatus.class);
        List<BikeStationRatingDto.BikeStationReview> reviews = jdbcTemplate.query(
                "SELECT user_id, rating, review FROM bikestationrating WHERE lendplace_id = ?",
                new BeanPropertyRowMapper<>(BikeStationRatingDto.BikeStationReview.class),
                lendplaceId
        );

        reviews.forEach(review -> {
            String username = jdbcTemplate.queryForObject(
                    "SELECT username FROM user WHERE id = ?",
                    new Object[]{review.getUser_id()},
                    String.class
            );

            review.setUsername(username);
        });

        BikeStationDto.BikeStationStatus bikeStationStatus = jdbcTemplate.queryForObject(
                "SELECT BS.lendplace_id, BS.statn_addr1, BS.statn_addr2, BS.startn_lat, BS.startn_lnt, " +
                        "       COALESCE(BC.available_bikes, 0) AS usable_bikes, " +
                        "       COALESCE(AVG(BR.rating), 0) AS average_rating, " +
                        "       CASE WHEN FAV.lendplace_id IS NOT NULL THEN TRUE ELSE FALSE END AS favorite " +
                        "FROM bikestationinformation BS " +
                        "LEFT JOIN ( " +
                        "    SELECT lendplace_id, COUNT(*) AS available_bikes " +
                        "    FROM bike " +
                        "    WHERE use_status = 0 AND bike_status = 1 " +
                        "    GROUP BY lendplace_id " +
                        ") AS BC ON BS.lendplace_id = BC.lendplace_id " +
                        "LEFT JOIN bikestationrating BR ON BS.lendplace_id = BR.lendplace_id " +
                        "LEFT JOIN favorite FAV ON BS.lendplace_id = FAV.lendplace_id AND FAV.user_id = ? " +
                        "WHERE BS.lendplace_id = ? " +
                        "GROUP BY BS.lendplace_id",
                bikeMapper,
                userId, lendplaceId
        );

        bikeStationStatus.setBikeStationReviews(reviews);

        return bikeStationStatus;

    }

    public void createBikeCountsView() {
        jdbcTemplate.execute(
                // VIEW, CASE
                "CREATE VIEW IF NOT EXISTS BikeCounts AS " +
                        "SELECT lendplace_id, COUNT(*) AS total_bikes, " +
                        "COUNT(CASE WHEN use_status=0 AND bike_status=1 THEN 1 END) AS usable_bikes " +
                        "FROM bike " +
                        "GROUP BY lendplace_id");
    }

    public List<BikeStationDto.BikeStationWithCurrentBike> findAll(int userId) {
        var bikeMapper = BeanPropertyRowMapper.newInstance(BikeStationDto.BikeStationWithCurrentBike.class);
        return jdbcTemplate.query(
                "SELECT BS.*, " +
                        "       COALESCE(BC.total_bikes, 0) AS total_bikes, " +
                        "       COALESCE(BC.usable_bikes, 0) AS usable_bikes, " +
                        "       COALESCE(AVG(BR.rating), 0) AS average_rating, " +
                        "       CASE WHEN FAV.lendplace_id IS NOT NULL THEN TRUE ELSE FALSE END AS favorite " +
                        "FROM bikestationinformation BS " +
                        "LEFT JOIN BikeCounts BC ON BS.lendplace_id = BC.lendplace_id " +
                        "LEFT JOIN bikestationrating BR ON BS.lendplace_id = BR.lendplace_id " +
                        "LEFT JOIN (SELECT lendplace_id FROM favorite WHERE user_id = ?) FAV ON BS.lendplace_id = FAV.lendplace_id " +
                        "GROUP BY BS.lendplace_id",
                bikeMapper,
                userId);
    }

    public List<BikeStationDto.BikeStationSimpleWithState> findRecentByUserId(int userId) {
        var stationMapper = BeanPropertyRowMapper.newInstance(BikeStationDto.BikeStationSimpleWithState.class);
        return jdbcTemplate.query(
                // DISTICT, COALESCE, Subqueries in the FROM claues, ALL, GROUP BY, ORDER BY, LIMIT, NULL
                "SELECT DISTINCT BSI.lendplace_id, BSI.statn_addr1, BSI.statn_addr2, UL.time, COALESCE(AVG(BR.rating), 0) AS average_rating " +
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
                        "LEFT JOIN bikestationrating BR ON BSI.lendplace_id = BR.lendplace_id " +
                        "GROUP BY BSI.lendplace_id, BSI.statn_addr1, BSI.statn_addr2, UL.time " +
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
                "SELECT BSI.lendplace_id, BSI.statn_addr1, BSI.statn_addr2, BR.average_rating " +
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

    public Boolean findRentalStatusByUserId(int userId) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS ( " +
                        "SELECT 1 " +
                        "FROM userlog " +
                        "WHERE user_id = ? " +
                        "AND arrival_station IS NULL " +
                        ")",
                new Object[]{userId},
                Boolean.class
        );
    }
}
