package com.dbdb.dbdb.service;

import com.dbdb.dbdb.dto.OAuthToken;
import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Slf4j
@Service
public class KakaoLoginService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Environment env;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // 액세스 토큰 요청
    public JsonNode getAccessTokenResponse(String code) {

        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader object 생성
        HttpHeaders headers = new HttpHeaders();;
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody object 생성
        String clientId = env.getProperty("oauth.kakao.client-id");
        String redirectUri = env.getProperty("oauth.kakao.redirect-uri");
        String resourceUri = env.getProperty("oauth.kakao.resource-uri");

        // 확인을 위한 log
        log.info("clientId = {}", clientId);
        log.info("redirectUri = {}", redirectUri);
        log.info("resourceUri = {}", resourceUri);

        // HttpHeader와 HttpBody를 하나의 obejct에 담기
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(params, headers);

        JsonNode response = restTemplate.exchange(
                resourceUri,
                HttpMethod.POST,
                accessTokenRequest,
                JsonNode.class
        ).getBody();

        return response;
    }

    // 액세스 토큰을 얻었으므로 파싱하여 반환
    public String parshingAccessToken(JsonNode responseBody){
        return responseBody.get("access_token").asText();
    }

    // 액세스 토큰을 사용하여 로그인한 유저에 대한 정보 요청
    public JsonNode getUserInfoByAccessTokenResponse(JsonNode accessToken) throws JsonProcessingException {

        // json object를 자바에서 처리하기 위한 변환 과정
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = objectMapper.readValue(accessToken.toString(), OAuthToken.class);
        log.info("accesstoken = {}", oAuthToken.getAccess_token());

        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader object 생성
        HttpHeaders headers = new HttpHeaders();;
        headers.add("Authorization", "Bearer "+oAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);

        String userResourceUri = env.getProperty("oauth.kakao.user-resource-uri");

        return restTemplate.exchange(userResourceUri, HttpMethod.POST, userInfoRequest, JsonNode.class).getBody(); // 유저 정보를 json으로 가져옴.
    }

    // 얻은 유저에 대한 정보에서 필요한 것들만 파싱
    public UserDto parshingUserInfo(JsonNode userResourceNode) throws JsonProcessingException {
        log.info("userResorceNode = {}", userResourceNode);

        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("kakao_account").get("email").asText();
        String nickName = userResourceNode.get("properties").get("nickname").asText();
        String is_email_verified = userResourceNode.get("kakao_account").get("is_email_verified").asText();
        String profile_image = userResourceNode.get("properties").get("profile_image").asText();

        // 카카오 서버 상에서 검증된 이메일인 경우
        if(is_email_verified.equals("true")){
            UserDto userDto = userRepository.findUserByEmail(email);
            if(userDto != null)
                return userDto;
            else{
                UserDto newKakaoUserDto = new UserDto();
                newKakaoUserDto.setEmail(email);
                newKakaoUserDto.setUsername(nickName);
                newKakaoUserDto.setUser_type(2);
                newKakaoUserDto.setPassword(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()));
                userRepository.insertUser(newKakaoUserDto);
                return newKakaoUserDto;
            }
        }

        return null;
    }

    public String kakaoLogout(String accessToken){
        RestTemplate restTemplate = new RestTemplate();
        log.info("accessToken in kakaoLogout = {}", accessToken);
        // HttpHeader object 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> logoutRequest = new HttpEntity<>(headers);

        String logoutResourceUri = env.getProperty("oauth.kakao.logout-resource-uri");

        ResponseEntity<String> responseId = restTemplate.exchange( // 로그아웃의 정상적인 return 값은 Id
                logoutResourceUri,
                HttpMethod.POST,
                logoutRequest,
                String.class
        );

        return responseId.getBody();
    }
}
