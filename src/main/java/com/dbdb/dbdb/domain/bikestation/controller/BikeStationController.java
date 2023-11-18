package com.dbdb.dbdb.domain.bikestation.controller;

import com.dbdb.dbdb.domain.bikestation.dto.BikeStationDto;
import com.dbdb.dbdb.domain.bikestation.service.BikeStationService;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping("/modify-lendplace")
    public ResponseEntity<JsonResponse> modifyBikeStation(@RequestBody BikeStationDto.BikeStationDetailDto bikeStationModifyDto) {
        bikeStationService.modifyBikeStation(bikeStationModifyDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @GetMapping("/get-lendplace")
    public ResponseEntity<JsonResponse> getBikeStation(@RequestParam String lendplace_id) {
        BikeStationDto.BikeStationWithBikeDto bikestation = bikeStationService.getBikeStation(lendplace_id);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, bikestation));
    }

    @DeleteMapping("/delete-lendplace")
    public ResponseEntity<JsonResponse> deleteBikeStation(@RequestBody BikeStationDto.BikeStationDeleteDto bikeStationDeleteDto) {
        bikeStationService.deleteBikeStation(bikeStationDeleteDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, bikeStationDeleteDto.getLendplace_id()));
    }


    @GetMapping("/search-lendplace")
    public ResponseEntity<JsonResponse> searchStation(@RequestParam String name) {
        List<BikeStationDto.BikeStationDetailDto> bikeStationDetailDtoList = bikeStationService.searchStation(name);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, bikeStationDetailDtoList));
    }
}
