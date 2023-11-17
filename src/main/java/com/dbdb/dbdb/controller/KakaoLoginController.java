package com.dbdb.dbdb.controller;

import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.service.KakaoLoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    @Autowired
    private KakaoLoginService kakaoLoginService;

    // ���� �ڵ� ��ȯ �׽�Ʈ��
    // https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=1534887d85f1d525b986e2521f7309b7&redirect_uri=http://localhost:8080/login/oauth2/code/kakao
//    @GetMapping("/code/kakao")
//    public @ResponseBody String kakaoCallback(@RequestParam String code){
//        return code;
//    }

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        JsonNode accessTokenResponse = kakaoLoginService.getAccessTokenResponse(code); // code�� ���� ���� response(access token�� ���� key�� ����)
        String accessToken = kakaoLoginService.parshingAccessToken(accessTokenResponse);
        JsonNode userInfoResponse = kakaoLoginService.getUserInfoByAccessTokenResponse(accessTokenResponse); // access token�� ���� ���� response(���� ���� ����)

        UserDto userDto = kakaoLoginService.parshingUserInfo(userInfoResponse);

        // ��Ű ����
        Cookie idCookie = new Cookie("id", String.valueOf(userDto.getId()));
        Cookie emailCookie = new Cookie("email", userDto.getEmail());
        Cookie passwordCookie = new Cookie("password", userDto.getPassword());

        // ��Ű ��ȿ �ð� ����
        idCookie.setMaxAge(7 * 24 * 60 * 60); // 7��
        emailCookie.setMaxAge(7 * 24 * 60 * 60);
        passwordCookie.setMaxAge(7 * 24 * 60 * 60);

        // ��Ű�� HttpOnly ����
        idCookie.setHttpOnly(true);
        emailCookie.setHttpOnly(true);
        passwordCookie.setHttpOnly(true);

        // ��Ű ��� ����
        idCookie.setPath("/");
        emailCookie.setPath("/");
        passwordCookie.setPath("/");

        // ���信 ��Ű �߰�
        response.addCookie(idCookie);
        response.addCookie(emailCookie);
        response.addCookie(passwordCookie);

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_KAKAO_LOGIN, null));
    }

    // �α׾ƿ�
    @PostMapping("/users/kakao-signout")
    public ResponseEntity<?> kakaoLogout(HttpServletRequest request, HttpServletResponse response) {
        kakaoLoginService.kakaoLogout()
        return ResponseEntity.ok().body(new JsonResponse<>(ResponseStatus.SUCCESS_LOGOUT));
    }
}