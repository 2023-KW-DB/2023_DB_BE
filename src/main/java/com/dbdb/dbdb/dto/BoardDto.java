package com.dbdb.dbdb.dto;

import com.dbdb.dbdb.table.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class GetBoardTitleDto{
        private int id;
        private int category_id;
        private String user_name;
        private int views;
        private String title;
        private boolean is_notice;
        private LocalDateTime created_at;
    }
}
