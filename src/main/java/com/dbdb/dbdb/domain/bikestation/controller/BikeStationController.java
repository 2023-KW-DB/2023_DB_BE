package com.dbdb.dbdb.domain.bikestation.controller;

import com.dbdb.dbdb.domain.bikestation.dto.BikeStationDto;
import com.dbdb.dbdb.domain.bikestation.service.BikeStationService;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/station")
public class BikeStationController {

    private final BikeStationService bikeStationService;

    @PostMapping("/create-lendplace")
    public ResponseEntity<JsonResponse> createBikeStation(@RequestBody BikeStationDto.BikeStationDetailDto bikeStationCreateDto) {
        bikeStationService.createBikeStation(bikeStationCreateDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @DeleteMapping("/delete-lendplace")
    public ResponseEntity<JsonResponse> deleteBikeStation(@RequestBody BikeStationDto.BikeStationDeleteDto bikeStationDeleteDto) {
        bikeStationService.deleteBikeStation(bikeStationDeleteDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, bikeStationDeleteDto.getLendplace_id()));
    }

}
