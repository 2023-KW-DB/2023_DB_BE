package com.dbdb.dbdb.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private int id;
    private String password;
    private String username;
    private int user_type;
    private String email;
    private String phone_number;
    private double weight;
    private int age;
    private LocalDateTime last_accessed_at; // 최근 접속 시간인 것 같은데 로그인 성공한 순간으로?
    private int total_money;
}
