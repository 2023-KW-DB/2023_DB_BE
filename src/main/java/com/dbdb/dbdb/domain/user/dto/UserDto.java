package com.dbdb.dbdb.domain.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private int id;
    private String password;
    private String username;
    private int user_type;
    private String email;
    private String phone_number;
    private Double weight;
    private int age;
    private LocalDateTime last_accessed_at; // �ֱ� ���� �ð��� �� ������ �α��� ������ ��������?
    private int total_money;
    private String fcm_token; // FCM token

    @NoArgsConstructor
    @Data
    public static class UserNameTypeDto {
        String username;
        int user_type;
    }
}
