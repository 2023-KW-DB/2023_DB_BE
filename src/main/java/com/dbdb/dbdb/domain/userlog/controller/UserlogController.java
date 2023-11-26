package com.dbdb.dbdb.domain.userlog.controller;

import com.dbdb.dbdb.domain.user.service.UserService;
import com.dbdb.dbdb.domain.userlog.dto.UserlogDto;
import com.dbdb.dbdb.domain.userlog.service.UserlogService;
import com.dbdb.dbdb.fcm.FCMService;
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
    @Autowired
    private FCMService fcmService;
    @Autowired
    private UserService userService;

    // 자전거 대여
    @PostMapping("/users/bike-rental")
    public ResponseEntity<?> bikeRental(@RequestBody UserlogDto userlogDto){

        String response = userlogService.bikeRental(userlogDto);

        if(response=="FAILED_NO_VALID_TICKET"){
            fcmService.sendBikeRentalFailedMessage(userService.findUserEmailById(userlogDto.getUser_id()));
            return ResponseEntity.ok(new JsonResponse(ResponseStatus.FAILED_NO_VALID_TICKET, null));
        }
        else if(response=="FAILED_INVALID_RENTAL_STATION"){
            fcmService.sendBikeRentalFailedMessage(userService.findUserEmailById(userlogDto.getUser_id()));
            return ResponseEntity.ok(new JsonResponse(ResponseStatus.FAILED_INVALID_RENTAL_STATION, null));
        }
        else if(response=="FAILED_INVALID_BIKE"){
            fcmService.sendBikeRentalFailedMessage(userService.findUserEmailById(userlogDto.getUser_id()));
            return ResponseEntity.ok(new JsonResponse(ResponseStatus.FAILED_INVALID_BIKE, null));
        }

        fcmService.sendBikeRentalSuccessMessage(userService.findUserEmailById(userlogDto.getUser_id()));
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS_BIKE_RENTAL, null));
    }

    // 자전거 반납
    @PostMapping("/users/bike-return")
    public ResponseEntity<?> bikeReturn(@RequestBody UserlogDto userlogDto){

        String response = userlogService.bikeReturn(userlogDto);

        if(response=="FAILED_INVALID_RETURN_STATION"){
            fcmService.sendBikeReturnFailedMessage(userService.findUserEmailById(userlogDto.getUser_id()));
            return ResponseEntity.ok(new JsonResponse(ResponseStatus.FAILED_INVALID_RETURN_STATION, null));
        }
        else if(response=="FAILED_OVER_MAX_STANDS"){
            fcmService.sendBikeReturnFailedMessage(userService.findUserEmailById(userlogDto.getUser_id()));
            return ResponseEntity.ok(new JsonResponse(ResponseStatus.FAILED_OVER_MAX_STANDS, null));
        }

        fcmService.sendBikeReturnSuccessMessage(userService.findUserEmailById(userlogDto.getUser_id()));
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS_BIKE_RETURN, null));
    }
}
