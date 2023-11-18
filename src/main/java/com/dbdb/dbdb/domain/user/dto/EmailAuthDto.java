package com.dbdb.dbdb.domain.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmailAuthDto {
    private int user_id;
    private String email;
    private int auth_num;
    private LocalDateTime created_at;
}