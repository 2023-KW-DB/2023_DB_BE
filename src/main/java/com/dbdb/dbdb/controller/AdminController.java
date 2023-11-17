package com.dbdb.dbdb.controller;

import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.service.AdminService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 모든 유저 정보 확인
    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers(){
        List<UserDto> users = adminService.getAllUsers();

        if (users.isEmpty())
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_ALL_USERS_INFO_ISEMPTY, null));

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_ALL_USERS_INFO, users));
    }

    // 유저 정보 수정
    @PatchMapping("/modify-user")
    public ResponseEntity<?> modifyUser(@RequestBody UserDto userDto){

        adminService.modifyUser(userDto);
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_MODIFY_USER_INFO, null));
    }
}
