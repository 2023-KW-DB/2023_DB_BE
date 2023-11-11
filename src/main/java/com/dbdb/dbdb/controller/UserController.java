package com.dbdb.dbdb.controller;

import com.dbdb.dbdb.dto.EmailAuthCodeDto;
import com.dbdb.dbdb.dto.EmailAuthDto;
import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.service.ChangePasswordService;
import com.dbdb.dbdb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestControllerAdvice
@RequestMapping("/users")
public class UserController {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

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
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.LOGIN_ERROR, null));
    }

    @PostMapping("/send-authcode") // 비밀번호 찾기 중 인증 번호 전송
    public ResponseEntity<?> sendAuthCode(@RequestBody UserDto userDto) throws MessagingException, UnsupportedEncodingException {

        EmailAuthCodeDto emailAuthCodeDto = new EmailAuthCodeDto();
        
        // 5분 이내에 다시 인증 번호 전송을 했다면 앞서 요청한 인증 번호 삭제
        changePasswordService.deleteExistCode(userDto.getEmail());

        emailAuthCodeDto.setAuthCode(changePasswordService.sendEmail(userDto.getEmail()));

        //executorService.schedule(changePasswordService::deleteExpiredAuthNum, 5, TimeUnit.MINUTES);

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_SEND_AUTHCODE));
    }

//    // 사용자가 입력한 인증 코드와 db의 인증 정보 비교
//    @PostMapping("/check-authcode") // 전송한 인증 번호 확인
//    public BaseResponse<EmailAuthCodeCheckDto> checkCode(@RequestBody EmailAuthCodeCheckDto emailAuthCodeCheckDto){
//
//        // 이메일을 입력하지 않은 경우
//        if(emailAuthCodeCheckDto.getAuthCode() == null)
//            return new BaseResponse<>(BaseResponseStatus.FAILED_INVALID_INPUT);
//
//        // 이메일 회원이 아닌 경우
//        if(!emailService.isUserTypeEmail(emailAuthCodeCheckDto.getEmail()))
//            return new BaseResponse<>(BaseResponseStatus.FAILED_NOT_EMAIL_USER);
//
//        String response = emailService.verifyCode(emailAuthCodeCheckDto.getEmail(), emailAuthCodeCheckDto.getAuthCode());
//
//        // 인증번호가 생성된지 5분이 되어 만료된 상황에서 인증 번호를 입력한 경우
//        if(response.equals("failed: over 5 minute"))
//            return new BaseResponse<>(BaseResponseStatus.FAILED_OVERTIME_AUTHCODE);
//
//        // 인증 번호가 틀린 경우
//        if(response.equals("failed: not correct auth code"))
//            return new BaseResponse<>(BaseResponseStatus.FAILED_NOT_CORRECT_AUTHCODE);
//
//        return new BaseResponse<>(emailAuthCodeCheckDto, BaseResponseStatus.SUCCESS_CHECK_AUTHCODE);
//    }
//
//    @PutMapping("/change-password") // 인증 번호 확인 후 비밀 번호 변경
//    public BaseResponse<PasswordChangeRequestDto> changePassword(@RequestBody PasswordChangeRequestDto passwordChangeRequestDto){
//
//        // 회원으로 등록되지 않은 이메일인 경우
//        if(userService.findByEmail(passwordChangeRequestDto.getEmail()) == null)
//            return new BaseResponse<>(BaseResponseStatus.FAILED_NOT_FOUND_USER);
//
//        // 이메일 유저가 아닌 경우
//        if(!emailService.isUserTypeEmail(passwordChangeRequestDto.getEmail()))
//            return new BaseResponse<>(BaseResponseStatus.FAILED_NOT_EMAIL_USER);
//
//        // 새로운 비밀번호를 입력하지 않은 경우
//        if(passwordChangeRequestDto.getPassword() == null || passwordChangeRequestDto.getPassword().equals(""))
//            return new BaseResponse<>(BaseResponseStatus.FAILED_INVALID_INPUT);
//
//        String response = emailService.changePassword(passwordChangeRequestDto.getEmail(), passwordChangeRequestDto.getPassword());
//
//        // server나 db 상의 이유로 비밀번호 변경을 실패한 경우
//        if(!response.equals("success: change password"))
//            return new BaseResponse<>(BaseResponseStatus.FAILED_CHANGE_PASSWORD);
//
//        return new BaseResponse<>(BaseResponseStatus.SUCCESS_CHANGE_PASSWORD);
//    }
//
//    @PreDestroy // 스케줄러 소멸
//    public void scheduledExecutorDestroy() {
//        executorService.shutdown();
//    }
}
