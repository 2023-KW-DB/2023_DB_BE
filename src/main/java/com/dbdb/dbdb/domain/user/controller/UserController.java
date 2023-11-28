package com.dbdb.dbdb.domain.user.controller;

import com.dbdb.dbdb.domain.user.dto.EmailAuthDto;
import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.fcm.FCMService;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.domain.user.service.ChangePasswordService;
import com.dbdb.dbdb.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestControllerAdvice
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ChangePasswordService changePasswordService;
    @Autowired
    private FCMService fcmService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserDto userDto) {

        UserDto user = userService.findUserByEmail(userDto.getEmail());
        if(user == null){
            userService.signUp(userDto);
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_SIGNUP, null));
        }
        else
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_NOT_SIGNUP, null));
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
    public ResponseEntity<?> signIn(@RequestBody UserDto userDto, HttpServletResponse response){
        if (userService.signIn(userDto)){
            // 쿠키 생성
            Cookie idCookie = new Cookie("id", String.valueOf(userService.findUserIdByEmail(userDto.getEmail())));
            Cookie emailCookie = new Cookie("email", userDto.getEmail());
            Cookie passwordCookie = new Cookie("password", userDto.getPassword());
            Cookie usernameCookie = new Cookie("username", userDto.getUsername());

            fcmService.saveTokenByObject(userDto);
            fcmService.sendLogincompletedMessage(userDto.getEmail());


            // 쿠키 유효 시간 설정
            idCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
            emailCookie.setMaxAge(7 * 24 * 60 * 60);
            passwordCookie.setMaxAge(7 * 24 * 60 * 60);
            usernameCookie.setMaxAge(7 * 24 * 60 * 60);

            // 쿠키에 HttpOnly 설정
//            idCookie.setHttpOnly(true);
//            emailCookie.setHttpOnly(true);
//            passwordCookie.setHttpOnly(true);
//            usernameCookie.setHttpOnly(true);

            // 쿠키 경로 설정
            idCookie.setPath("/");
            emailCookie.setPath("/");
            passwordCookie.setPath("/");
            usernameCookie.setPath("/");

            // 응답에 쿠키 추가
            response.addCookie(idCookie);
            response.addCookie(emailCookie);
            response.addCookie(passwordCookie);
            response.addCookie(usernameCookie);

            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_LOGIN, null));
        }
        else
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.ERROR_LOGIN, null));
    }

    // 로그아웃
    @PostMapping("/signout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("id".equals(cookie.getName()) || "email".equals(cookie.getName()) || "password".equals(cookie.getName()) || "username".equals(cookie.getName())) {
                    if("email".equals(cookie.getName())){
                        fcmService.sendLogoutcompletedMessage(cookie.getValue());
                        fcmService.deleteToken(cookie.getValue());
                    }

                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
            return ResponseEntity.ok().body(new JsonResponse<>(ResponseStatus.SUCCESS_LOGOUT));
        }

        return ResponseEntity.ok().body(new JsonResponse<>(ResponseStatus.COOKIE_ERROR));
    }

    // 회원탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<?> withdrawal(@RequestBody UserDto userDto){
        userService.withdrawal(userDto.getId());

        return ResponseEntity.ok().body(new JsonResponse<>(ResponseStatus.SUCCESS_WITHDRAWAL));
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

    @GetMapping("/get-userinfo")
    public ResponseEntity<?> getUserInfoById(@RequestParam int user_id){
        UserDto userInfo = userService.findUserByid(user_id);

        if (userInfo == null)
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_NOT_FIND_USER_BY_ID, null));
        else
            return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_FIND_USER_BY_ID, userInfo));
    }
}
