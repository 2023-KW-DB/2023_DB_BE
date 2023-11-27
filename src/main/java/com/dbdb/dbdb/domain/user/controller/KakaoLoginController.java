package com.dbdb.dbdb.domain.user.controller;

import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.domain.user.service.KakaoLoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    @Autowired
    private KakaoLoginService kakaoLoginService;


    // kakao login
    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        JsonNode accessTokenResponse = kakaoLoginService.getAccessTokenResponse(code); // code를 통해 얻은 response(access token과 여러 key들 존재)
        String accessToken = kakaoLoginService.parshingAccessToken(accessTokenResponse);
        JsonNode userInfoResponse = kakaoLoginService.getUserInfoByAccessTokenResponse(accessTokenResponse); // access token을 통해 얻은 response(유저 정보 존재)

        UserDto userDto = kakaoLoginService.parshingUserInfo(userInfoResponse);

        HttpSession session = request.getSession();
        session.setAttribute("access_token", accessToken);

        Cookie idCookie = new Cookie("id", String.valueOf(userDto.getId()));
        Cookie emailCookie = new Cookie("email", userDto.getEmail());
        Cookie passwordCookie = new Cookie("password", userDto.getPassword());
        Cookie usernameCookie = new Cookie("username", userDto.getUsername());
        //Cookie accessTokenCookie = new Cookie("access_token", accessToken);
        log.info("idCookie = {}", idCookie.getValue());
        log.info("emailCookie = {}", emailCookie.getValue());
        log.info("passwordCookie = {}", passwordCookie.getValue());

        idCookie.setMaxAge(7 * 24 * 60 * 60);
        emailCookie.setMaxAge(7 * 24 * 60 * 60);
        passwordCookie.setMaxAge(7 * 24 * 60 * 60);
        usernameCookie.setMaxAge(7 * 24 * 60 * 60);

        idCookie.setHttpOnly(true);
        emailCookie.setHttpOnly(true);
        passwordCookie.setHttpOnly(true);
        usernameCookie.setHttpOnly(true);

        idCookie.setPath("/");
        emailCookie.setPath("/");
        passwordCookie.setPath("/");
        usernameCookie.setPath("/");

        response.addCookie(idCookie);
        response.addCookie(emailCookie);
        response.addCookie(passwordCookie);
        response.addCookie(usernameCookie);
        //response.addCookie(accessTokenCookie);

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS_KAKAO_LOGIN, null));
    }

    // kakao logout
    @PostMapping("/users/kakao-signout")
    public ResponseEntity<?> kakaoLogout(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("id".equals(cookie.getName()) || "email".equals(cookie.getName()) || "password".equals(cookie.getName()) || "username".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        else
            log.info("cookie is null!!!");

        HttpSession session = request.getSession(false);
        String responseId = "";
        if (session != null) {
            log.info("session is not null");
            String accessToken = (String) session.getAttribute("access_token");
            if (accessToken != null) {
                responseId = kakaoLoginService.kakaoLogout(accessToken);
                log.info("responseId = {}", responseId);
                session.invalidate();
                return ResponseEntity.ok().body(new JsonResponse<>(ResponseStatus.SUCCESS_LOGOUT));
            }
            return ResponseEntity.ok().body(new JsonResponse<>(ResponseStatus.ACCESS_TOKEN_NULL));
        }
        else
            return ResponseEntity.ok().body(new JsonResponse<>(ResponseStatus.SESSION_ERROR));
    }
}