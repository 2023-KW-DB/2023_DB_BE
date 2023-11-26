package com.dbdb.dbdb.domain.bikestationrating.service;

import com.dbdb.dbdb.domain.bikestationrating.dto.BikeStationRatingDto;
import com.dbdb.dbdb.domain.bikestationrating.repository.BikeStationRatingRepository;
import com.dbdb.dbdb.domain.bikestationrating.table.BikeStationRating;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BikeStationRatingService {

    private final BikeStationRatingRepository bikeStationRatingRepository;
    public void createRating(BikeStationRatingDto.BikeStationRatingRequest bikeStationRatingRequest) {
        try {
            if (bikeStationRatingRequest.getLendplace_id1() != null && !bikeStationRatingRequest.getLendplace_id1().isBlank()) {
                BikeStationRating bikeStationRating = new BikeStationRating(
                        bikeStationRatingRequest.getLendplace_id1(),
                        bikeStationRatingRequest.getUser_id(),
                        bikeStationRatingRequest.getRating1(),
                        bikeStationRatingRequest.getReview1()
                );
                bikeStationRatingRepository.insert(bikeStationRating);
            }
            if (bikeStationRatingRequest.getLendplace_id2() != null && !bikeStationRatingRequest.getLendplace_id2().isBlank()) {
                BikeStationRating bikeStationRating = new BikeStationRating(
                        bikeStationRatingRequest.getLendplace_id2(),
                        bikeStationRatingRequest.getUser_id(),
                        bikeStationRatingRequest.getRating2(),
                        bikeStationRatingRequest.getReview2()
                );
                bikeStationRatingRepository.insert(bikeStationRating);
            }
        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }
}
