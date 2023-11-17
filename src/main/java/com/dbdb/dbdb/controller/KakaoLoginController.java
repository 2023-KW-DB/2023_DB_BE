package com.dbdb.dbdb.controller;

import com.dbdb.dbdb.service.KakaoLoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class KakaoLoginController {

    @Autowired
    private KakaoLoginService kakaoLoginService;

    // ���� �ڵ� ��ȯ �׽�Ʈ��
    // https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=1534887d85f1d525b986e2521f7309b7&redirect_uri=http://localhost:8080/login/oauth2/code/kakao
//    @GetMapping("/code/kakao")
//    public @ResponseBody String kakaoCallback(@RequestParam String code){
//        return code;
//    }

    @GetMapping("/code/kakao")
    public String kakaoCallback(@RequestParam String code, HttpServletRequest request) throws JsonProcessingException {
        JsonNode accessTokenResponse = kakaoLoginService.getAccessTokenResponse(code); // code�� ���� ���� response(access token�� ���� key�� ����)
        String accessToken = kakaoLoginService.parshingAccessToken(accessTokenResponse);
        JsonNode userInfoResponse = kakaoLoginService.getUserInfoByAccessTokenResponse(accessTokenResponse); // access token�� ���� ���� response(���� ���� ����)

        HttpSession session = request.getSession();

        UserEntity userEntity = kakaoLoginService.parshingUserInfo(userInfoResponse);
        session.setAttribute("accessToken", accessToken);
        session.setAttribute("id", userEntity.getUserId());
        session.setAttribute("email", userEntity.getEmail());

        if (userEntity!=null){
            kakaoLoginService.save(userEntity);
        }

        return "kakao login success";
    }
}