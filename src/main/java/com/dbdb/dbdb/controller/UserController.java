package com.dbdb.dbdb.controller;

import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {
        userService.signUp(userDto);
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, null));
    }

    // 이메일 중복 확인
    @PostMapping("/email/check-duplicate")
    public ResponseEntity<?> checkEmailDuplicate(@RequestBody UserDto userDto) {
        if (userService.checkEmailDuplicate(userDto))
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.EMAIL_DUPLICATE, null));
        else
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.EMAIL_NOT_DUPLICATE, null));
    }
}
