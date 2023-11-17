package com.dbdb.dbdb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    public static class GetCommentDto{
        private int id;
        private String username;
        private int write_id;
        private int category_id;
        private String content;
        private int likeCount;
        private boolean userLiked;
        private LocalDateTime created_at;
        private LocalDateTime updated_at;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class DBReturnCommentDto{
        private int id;
        private int user_id;
        private int write_id;
        private int category_id;
        private String content;
        private LocalDateTime created_at;
        private LocalDateTime updated_at;
        private int likeCount;
        private boolean userLiked;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class CommentLikeDto{
        private int user_id;
        private int liked_id;
    }


}
