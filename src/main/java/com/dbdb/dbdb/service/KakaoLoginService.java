package com.dbdb.dbdb.service;

import com.dbdb.dbdb.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class KakaoLoginService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Environment env;

    // �׼��� ��ū ��û
    public JsonNode getAccessTokenResponse(String code) {

        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader object ����
        HttpHeaders headers = new HttpHeaders();;
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody object ����
        String clientId = env.getProperty("oauth2.kakao.client-id");
        String redirectUri = env.getProperty("oauth2.kakao.redirect-uri");
        String resourceUri = env.getProperty("oauth2.kakao.resource-uri");

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
}
