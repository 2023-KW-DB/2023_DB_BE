package com.dbdb.dbdb.domain.bikestation.repository;

import com.dbdb.dbdb.domain.bikestation.dto.BikeStationDto;
import lombok.RequiredArgsConstructor;
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
}
