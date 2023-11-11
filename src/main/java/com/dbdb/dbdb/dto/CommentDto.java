package com.dbdb.dbdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CommentDto {
    @NoArgsConstructor
    @Data
    public static class CreateCommentDto {
        private int user_id;
        private int write_id;
        private int category_id;
        private String content;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class CommentLikeDto{
        private int user_id;
        private int liked_id;
    }
}
