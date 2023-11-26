package com.dbdb.dbdb.domain.bikestationrating.controller;

import com.dbdb.dbdb.domain.bikestationrating.dto.BikeStationRatingDto;
import com.dbdb.dbdb.domain.bikestationrating.service.BikeStationRatingService;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rating")
public class BikeStationRatingController {
    private final BikeStationRatingService bikeStationRatingService;

    @PostMapping("/lendplace-review")
    public ResponseEntity<JsonResponse> reviewRating(@RequestBody BikeStationRatingDto.BikeStationRatingRequest bikeStationRatingRequest) {
        bikeStationRatingService.createRating(bikeStationRatingRequest);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

}