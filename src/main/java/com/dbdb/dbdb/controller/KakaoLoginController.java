package com.dbdb.dbdb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class KakaoLoginController {

    @GetMapping("/code/kakao")
    public @ResponseBody String kakaoCallback(@RequestParam String code){
        return code;
    }
}