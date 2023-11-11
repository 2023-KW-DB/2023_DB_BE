package com.dbdb.dbdb.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BoardDto {

    @NoArgsConstructor
    @Data
    public static class CreateBoardDto {
        private int category_id;
        private int user_id;
        private String title;
        private String content;
        private boolean is_notice;
        private String file_name;
        private String url;
    }
}
