package com.dbdb.dbdb.domain.bikestationrating.repository;

import com.dbdb.dbdb.domain.bikestationrating.dto.BikeStationRatingDto;
import com.dbdb.dbdb.domain.bikestationrating.table.BikeStationRating;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BikeStationRatingRepository {

    private final JdbcTemplate jdbcTemplate;

    public void insert(BikeStationRating bikeStationRating) {
        String sql = "INSERT INTO bikestationrating (`lendplace_id`, `user_id`, `rating`, `review`) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                bikeStationRating.getLendplace_id(), bikeStationRating.getUser_id(), bikeStationRating.getRating(), bikeStationRating.getReview()
        );
    }
}
