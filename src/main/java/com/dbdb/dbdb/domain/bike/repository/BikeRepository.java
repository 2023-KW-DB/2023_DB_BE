package com.dbdb.dbdb.domain.bike.repository;

import com.dbdb.dbdb.domain.bike.table.Bike;
import com.dbdb.dbdb.domain.board.table.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class BikeRepository {

    private final JdbcTemplate jdbcTemplate;
    public void insertBike(Bike bike) {
        jdbcTemplate.update("INSERT INTO `bike` (`lendplace_id`, `use_status`, `bike_status`) VALUES (?,?,?)"
                , bike.getLendplace_id(), bike.getUse_status(), bike.getBike_status());
    }
}
