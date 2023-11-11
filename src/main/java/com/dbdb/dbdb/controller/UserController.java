package com.dbdb.dbdb.controller;

import com.dbdb.dbdb.dto.EmailAuthDto;
import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.service.ChangePasswordService;
import com.dbdb.dbdb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestControllerAdvice
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ChangePasswordService changePasswordService;

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

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody UserDto userdto){
        if (userService.signIn(userdto))
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_LOGIN, null));
        else
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.ERROR_LOGIN, null));
    }

    // 비밀번호 찾기 중 인증 번호 전송
    @PostMapping("/send-authcode")
    public ResponseEntity<?> sendAuthCode(@RequestBody UserDto userDto) throws MessagingException, UnsupportedEncodingException {

        EmailAuthDto emailAuthCodeDto = new EmailAuthDto();
        
        // 5분 이내에 다시 인증 번호 전송을 했다면 앞서 요청한 인증 번호 삭제
        changePasswordService.deleteExistCode(userDto.getEmail());

        emailAuthCodeDto.setAuth_num(Integer.parseInt(changePasswordService.sendEmail(userDto.getEmail())));

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_SEND_AUTHCODE, emailAuthCodeDto.getAuth_num()));
    }

    // 입력한 인증 번호 검사
    @PostMapping("/check-authcode")
    public ResponseEntity<?> checkAuthCode(@RequestBody EmailAuthDto emailAuthDto){

        String response = changePasswordService.verifyCode(emailAuthDto.getEmail(), String.valueOf(emailAuthDto.getAuth_num()));

        if(response.equals("Error: over 5 minute")) // 인증번호가 생성된지 5분이 되어 만료된 상황에서 인증 번호를 입력한 경우
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.ERROR_TIMEOVER_AUTHCODE));
        else if(response.equals("Error: not correct auth code")) // 인증 번호가 틀린 경우
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_NOT_CORRECT_AUTHCODE));
        else // 인증 번호 일치
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_CORRECT_AUTHCODE));
    }

    // 인증 번호 확인 후 비밀 번호 변경
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody UserDto userDto){

        changePasswordService.changePassword(userDto.getEmail(), userDto.getPassword());

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_CHANGE_PASSWORD));
    }
}
