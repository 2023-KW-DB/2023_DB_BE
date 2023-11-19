package com.dbdb.dbdb.domain.userlog.controller;

import com.dbdb.dbdb.domain.userlog.dto.UserlogDto;
import com.dbdb.dbdb.domain.userlog.service.UserlogService;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserlogController {

    @Autowired
    private UserlogService userlogService;

    // 자전거 대여
    @PostMapping("/users/bike-rental")
    public ResponseEntity<?> bikeRental(@RequestBody UserlogDto userlogDto){
        String response = userlogService.bikeRental(userlogDto);


        if(response=="FAILED_NO_VALID_TICKET")
            return ResponseEntity.ok(new JsonResponse(ResponseStatus.FAILED_NO_VALID_TICKET, null));
        else if(response=="FAILED_INVALID_RENTAL_STATION")
            return ResponseEntity.ok(new JsonResponse(ResponseStatus.FAILED_INVALID_RENTAL_STATION, null));
        else if(response=="FAILED_INVALID_BIKE")
            return ResponseEntity.ok(new JsonResponse(ResponseStatus.FAILED_INVALID_BIKE, null));

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS_BIKE_RENTAL, null));
    }

//    // 자전거 반납
//    @PostMapping("/users/bike-return")
//    public ResponseEntity<?> bikeReturn(@RequestBody UserlogDto userlogDto){
//
//        String response = userlogService.bikeReturn();
//
//        if(response=="FAILED_RETURN_STATION")
//            return ResponseEntity.ok(new JsonResponse(ResponseStatus.FAILED_INVALID_RETURN_STATION, null));
//
//        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS_BIKE_RETURN, null));
//    }
}
