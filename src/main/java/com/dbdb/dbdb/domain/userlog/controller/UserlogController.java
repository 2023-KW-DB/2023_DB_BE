package com.dbdb.dbdb.domain.userlog.controller;

import com.dbdb.dbdb.domain.ticket.dto.TicketDto;
import com.dbdb.dbdb.domain.user.service.UserService;
import com.dbdb.dbdb.domain.userlog.dto.UserlogDto;
import com.dbdb.dbdb.domain.userlog.dto.VisualizationUserlogDto;
import com.dbdb.dbdb.domain.userlog.service.UserlogService;
import com.dbdb.dbdb.fcm.FCMService;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    // 해당 유저의 모든 로그 조회
    @GetMapping("/get-userlog")
    public ResponseEntity<?> getUserLog(@RequestParam int userId) {
        List<UserlogDto> userlog = userlogService.getUserlog(userId);

        if((userlog==null) || (userlog.isEmpty()))
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_USERLOG_EMPTY, null));

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS_GET_USERLOG, userlog));
    }

//    // 모든 유저의 모든 로그 조회
//    @GetMapping("/get-all-userlog")
//    public ResponseEntity<?> getAllUserLog(@RequestParam int userId) {
//        List<UserlogDto> allUserlog = userlogService.getAllUserlog();
//
//        if(allUserlog.isEmpty())
//            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_ALL_TICKETS_INFO_ISEMPTY, null));
//
//        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS_GET_USERLOG, allUserlog));
//    }

    @GetMapping("/get-highest-usetime")
    public ResponseEntity<?> getTopUseTime() {
        List<VisualizationUserlogDto.userUseTimeInfo> userLog = userlogService.getTopUseTime();
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userLog));
    }

    @GetMapping("/get-highest-usecount")
    public ResponseEntity<?> getTopUseCount() {
        List<VisualizationUserlogDto.userUseCountInfo> userLog = userlogService.getTopUseCount();
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userLog));
    }

    @GetMapping("/get-highest-distance")
    public ResponseEntity<?> getTopUseDistance() {
        List<VisualizationUserlogDto.userUseDistanceInfo> userLog = userlogService.getTopUseDistance();
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userLog));
    }

    @GetMapping("/get-between-userlog")
    public ResponseEntity<?> getBetweenUserlog(@RequestParam String start, @RequestParam String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);

        List<VisualizationUserlogDto.userLogDto> userLog = userlogService.getBetweenUserlog(startDateTime, endDateTime);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userLog));
    }

    // 모든 유저의 모든 로그 조회
    @GetMapping("/get-all-userlog")
    public ResponseEntity<?> getAllUserLog() {
        List<UserlogDto> allUserlog = userlogService.getAllUserlog();

        if((allUserlog==null) || (allUserlog.isEmpty()))
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_ALL_USERLOG_EMPTY, null));

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS_GET_ALL_USERLOG, allUserlog));
    }

}
