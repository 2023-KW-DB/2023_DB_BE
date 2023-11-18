package com.dbdb.dbdb.domain.bike.controller;

import com.dbdb.dbdb.domain.bike.dto.BikeDto;
import com.dbdb.dbdb.domain.bike.service.BikeService;
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
@RequestMapping("/bike")
public class BikeController {

    private final BikeService bikeService;

    @PostMapping("/create")
    public ResponseEntity<JsonResponse> createBike(@RequestBody BikeDto.BikeCreateDto bikeCreateDto) {
        bikeService.createBike(bikeCreateDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
