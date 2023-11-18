package com.dbdb.dbdb.domain.bike.repository;

import com.dbdb.dbdb.domain.bike.dto.BikeDto;
import com.dbdb.dbdb.domain.bike.table.Bike;
import com.dbdb.dbdb.domain.board.table.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BikeRepository {

    private final JdbcTemplate jdbcTemplate;
    public void insertBike(Bike bike) {
        jdbcTemplate.update("INSERT INTO `bike` (`lendplace_id`, `use_status`, `bike_status`) VALUES (?,?,?)"
                , bike.getLendplace_id(), bike.getUse_status(), bike.getBike_status());
    }

    public List<Bike> findAll() {
        var bikeMapper = BeanPropertyRowMapper.newInstance(Bike.class);

        return jdbcTemplate.query(
                "SELECT * FROM bike", bikeMapper
        );
    }

    public void update(BikeDto.BikeModifyDto bikeModifyDto) {
        jdbcTemplate.update("UPDATE `bike` SET " +
                        "lendplace_id=?, " +
                        "use_status= ?, " +
                        "bike_status=? " +
                        "WHERE id=?"
                , bikeModifyDto.getLendplace_id(), bikeModifyDto.getUse_status(), bikeModifyDto.getBike_status(), bikeModifyDto.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM `bike` WHERE id=?", id);
    }
}
