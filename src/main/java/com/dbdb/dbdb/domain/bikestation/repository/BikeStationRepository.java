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

    public BikeStationDto.BikeStationWithBikeDto findDetailByLendplaceId(String lendplaceId) {
        var bikeMapper = BeanPropertyRowMapper.newInstance(BikeStationDto.BikeStationWithBikeDto.class);
        return jdbcTemplate.queryForObject(
                "WITH BikeCounts AS (" +
                "    SELECT lendplace_id, COUNT(*) AS total_bikes " +
                "    FROM bike " +
                "    GROUP BY lendplace_id ) " +
                "SELECT BS.*, COALESCE(BC.total_bikes, 0) AS total_bikes, (BS.max_stands - COALESCE(BC.total_bikes, 0)) AS empty_stands " +
                "FROM bikestationinformation BS " +
                "LEFT JOIN BikeCounts BC ON BS.lendplace_id = BC.lendplace_id " +
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
}
