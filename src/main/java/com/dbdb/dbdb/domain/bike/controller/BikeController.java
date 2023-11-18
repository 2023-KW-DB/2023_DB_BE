package com.dbdb.dbdb.domain.bike.controller;

import com.dbdb.dbdb.domain.bike.dto.BikeDto;
import com.dbdb.dbdb.domain.bike.service.BikeService;
import com.dbdb.dbdb.domain.bike.table.Bike;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get-all")
    public ResponseEntity<JsonResponse> getAllBike() {
        List<Bike> bikeAllDtoList = bikeService.getAllBike();
        if(bikeAllDtoList.isEmpty()) throw new GlobalException(ResponseStatus.RESULT_NOT_EXIST);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, bikeAllDtoList));
    }

    @PatchMapping("/modify")
    public ResponseEntity<JsonResponse> modifyBike(@RequestBody BikeDto.BikeModifyDto bikeModifyDto) {
        bikeService.modifyBike(bikeModifyDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<JsonResponse> deleteBike(@RequestBody BikeDto.BikeDeleteDto bikeDeleteDto) {
        bikeService.deleteBike(bikeDeleteDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, bikeDeleteDto.getId()));
    }
}
