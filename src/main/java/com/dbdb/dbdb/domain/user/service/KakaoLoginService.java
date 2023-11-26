package com.dbdb.dbdb.domain.user.service;

import com.dbdb.dbdb.domain.user.dto.OAuthToken;
import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.domain.user.repository.UserRepository;
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

    // �׼��� ��ū ��û
    public JsonNode getAccessTokenResponse(String code) {

        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader object ����
        HttpHeaders headers = new HttpHeaders();;
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody object ����
        String clientId = env.getProperty("oauth.kakao.client-id");
        String redirectUri = env.getProperty("oauth.kakao.redirect-uri");
        String resourceUri = env.getProperty("oauth.kakao.resource-uri");

        // Ȯ���� ���� log
        log.info("clientId = {}", clientId);
        log.info("redirectUri = {}", redirectUri);
        log.info("resourceUri = {}", resourceUri);

        // HttpHeader�� HttpBody�� �ϳ��� obejct�� ���
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

    // �׼��� ��ū�� ������Ƿ� �Ľ��Ͽ� ��ȯ
    public String parshingAccessToken(JsonNode responseBody){
        return responseBody.get("access_token").asText();
    }

    // �׼��� ��ū�� ����Ͽ� �α����� ������ ���� ���� ��û
    public JsonNode getUserInfoByAccessTokenResponse(JsonNode accessToken) throws JsonProcessingException {

        // json object�� �ڹٿ��� ó���ϱ� ���� ��ȯ ����
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = objectMapper.readValue(accessToken.toString(), OAuthToken.class);
        log.info("accesstoken = {}", oAuthToken.getAccess_token());

        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader object ����
        HttpHeaders headers = new HttpHeaders();;
        headers.add("Authorization", "Bearer "+oAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);

        String userResourceUri = env.getProperty("oauth.kakao.user-resource-uri");

        return restTemplate.exchange(userResourceUri, HttpMethod.POST, userInfoRequest, JsonNode.class).getBody(); // ���� ������ json���� ������.
    }

    // ���� ������ ���� �������� �ʿ��� �͵鸸 �Ľ�
    public UserDto parshingUserInfo(JsonNode userResourceNode) throws JsonProcessingException {
        log.info("userResorceNode = {}", userResourceNode);

        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("kakao_account").get("email").asText();
        String nickName = userResourceNode.get("properties").get("nickname").asText();
        String is_email_verified = userResourceNode.get("kakao_account").get("is_email_verified").asText();
        String profile_image = userResourceNode.get("properties").get("profile_image").asText();

        // īī�� ���� �󿡼� ������ �̸����� ���
        if(is_email_verified.equals("true")){
            UserDto userDto = userRepository.findUserByEmail(email);
            if(userDto != null) {
                userDto.setLast_accessed_at(LocalDateTime.now());
                userRepository.insertUser(userDto);
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
                return newKakaoUserDto;
            }
        }

        return null;
    }

    public String kakaoLogout(String accessToken){
        RestTemplate restTemplate = new RestTemplate();
        log.info("accessToken in kakaoLogout = {}", accessToken);
        // HttpHeader object ����
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
