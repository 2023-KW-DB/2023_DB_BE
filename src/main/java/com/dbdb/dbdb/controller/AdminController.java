package com.dbdb.dbdb.controller;

import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.domain.user.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ��� ���� ���� Ȯ��
    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers(){
        List<UserDto> users = adminService.getAllUsers();

        if (users.isEmpty())
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_ALL_USERS_INFO_ISEMPTY, null));

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_GET_ALL_USERS_INFO, users));
    }

    // ���� ���� ����
    @PatchMapping("/modify-user")
    public ResponseEntity<?> modifyUser(@RequestBody UserDto userDto){

        adminService.modifyUser(userDto);
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_MODIFY_USER_INFO, null));
    }

    // ���� ���� ����
    @DeleteMapping("/delete-user")
    public ResponseEntity<?> deleteUser(@RequestBody UserDto userDto){

        adminService.deleteUser(userDto.getId());

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_DELETE_USER, null));
    }
}
