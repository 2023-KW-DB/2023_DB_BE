package com.dbdb.dbdb.domain.coupon.controller;

import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class TicketController {

    // 이용권 종류 추가
    @PostMapping("/create-ticket")
    public ResponseEntity<?> createTicket(@RequestBody UserDto userDto) {


    }

    // 이용권 종류 추가
    @PatchMapping("/modify-ticket")
    public ResponseEntity<?> modifyTicket(@RequestBody UserDto userDto) {


    }

    // 이용권 종류 추가
    @DeleteMapping("/delete-ticket")
    public ResponseEntity<?> deleteTicket(@RequestBody UserDto userDto) {


    }
}
