package com.dbdb.dbdb.domain.user.service;

import com.dbdb.dbdb.domain.user.dto.OAuthToken;
import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.domain.user.repository.UserRepository;
import com.dbdb.dbdb.fcm.FCMService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;
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
    @Autowired
    private FCMService fcmService;

    private String fcm_token;

    // request access token
    public JsonNode getAccessTokenResponse(String fcm, String code) {
        fcm_token = fcm;
        RestTemplate restTemplate = new RestTemplate();

        // generate HttpHeader object
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // generate HttpBody object
        String clientId = env.getProperty("oauth.kakao.client-id");
        String redirectUri = env.getProperty("oauth.kakao.redirect-uri");
        String resourceUri = env.getProperty("oauth.kakao.resource-uri");

        // log(for check)
        log.info("clientId = {}", clientId);
        log.info("redirectUri = {}", redirectUri);
        log.info("resourceUri = {}", resourceUri);

        // HttpHeader and HttpBody in one objct
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

    // Parse and return the access token as it has been obtained
    public String parshingAccessToken(JsonNode responseBody){
        return responseBody.get("access_token").asText();
    }

    // Request information about users logged in using access tokens
    public JsonNode getUserInfoByAccessTokenResponse(JsonNode accessToken) throws JsonProcessingException {

        // Transformation process for processing json objects in Java
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = objectMapper.readValue(accessToken.toString(), OAuthToken.class);
        log.info("accesstoken = {}", oAuthToken.getAccess_token());

        RestTemplate restTemplate = new RestTemplate();

        // generate HttpHeader object
        HttpHeaders headers = new HttpHeaders();;
        headers.add("Authorization", "Bearer "+oAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);

        String userResourceUri = env.getProperty("oauth.kakao.user-resource-uri");

        // get user information by json
        return restTemplate.exchange(userResourceUri, HttpMethod.POST, userInfoRequest, JsonNode.class).getBody();
    }

    // Parsing only what you need from information about the user
    public UserDto parshingUserInfo(JsonNode userResourceNode) throws JsonProcessingException {
        log.info("userResorceNode = {}", userResourceNode);

        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("kakao_account").get("email").asText();
        String nickName = userResourceNode.get("properties").get("nickname").asText();
        String is_email_verified = userResourceNode.get("kakao_account").get("is_email_verified").asText();
        String profile_image = userResourceNode.get("properties").get("profile_image").asText();
        log.info("fcm_token={}", fcm_token);
        // If this is a verified email on the Kakao server
        if(is_email_verified.equals("true")){
            UserDto userDto = userRepository.findUserByEmail(email);
            if(userDto != null) {
                userDto.setLast_accessed_at(LocalDateTime.now());
                fcmService.saveTokenByObject(userDto);
                userRepository.updateLastAccessedAt(userDto);
                return userDto;
            }
            else{
                UserDto newKakaoUserDto = new UserDto();
                newKakaoUserDto.setEmail(email);
                newKakaoUserDto.setUsername(nickName);
                newKakaoUserDto.setUser_type(2);
                newKakaoUserDto.setLast_accessed_at(LocalDateTime.now());
                newKakaoUserDto.setPassword(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()));
                userRepository.insertUser(newKakaoUserDto);
                fcmService.saveTokenByVariable(fcm_token, email);
                fcmService.sendLogincompletedMessage(newKakaoUserDto.getEmail());
                return userRepository.findUserByEmail(email);
            }
        }

        return null;
    }

    public String kakaoLogout(String accessToken){
        RestTemplate restTemplate = new RestTemplate();
        log.info("accessToken in kakaoLogout = {}", accessToken);
        // generate HttpHeader object
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> logoutRequest = new HttpEntity<>(headers);

        String logoutResourceUri = env.getProperty("oauth.kakao.logout-resource-uri");

        ResponseEntity<String> responseId = restTemplate.exchange( // �α׾ƿ��� �������� return ���� Id
                logoutResourceUri,
                HttpMethod.POST,
                logoutRequest,
                String.class
        );

        return responseId.getBody();
    }
}
