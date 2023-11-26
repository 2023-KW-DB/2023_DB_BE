package com.dbdb.dbdb.domain.board.dto;

import com.dbdb.dbdb.domain.comment.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BoardDto {

    @NoArgsConstructor
    @Data
    public static class CreateBoardDto {
        private int category_id;
        private int user_id;
        private String title;
        private String content;
        private boolean notice;
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
        private boolean notice;
        private LocalDateTime created_at;
        private int commentCount;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class GetBoardDto{
        private int id;
        private int category_id;
        private String user_name;
        private int views;
        private String title;
        private String content;
        private boolean notice;
        private String file_name;
        private String url;
        private int likeCount;
        private boolean userLiked;
        private LocalDateTime created_at;
        private LocalDateTime updated_at;
        private List<CommentDto.GetCommentDto> commentDtoList;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class ModifyBoardDto{
        private int id;
        private int category_id;
        private int user_id;
        private String title;
        private String content;
        private boolean notice;
        private String file_name;
        private String url;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class BoardLikeDto{
        private int user_id;
        private int category_id;
        private int liked_id;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class BoardWithCommentsCount {
        private int id;
        private int category_id;
        private int user_id;
        private int views;
        private String title;
        private boolean notice;
        private LocalDateTime created_at;
        private int comment_count;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class BoardWithLike {
        private int id;
        private int category_id;
        private int user_id;
        private int views;
        private String title;
        private String content;
        private boolean notice;
        private String file_name;
        private String url;
        private LocalDateTime created_at;
        private LocalDateTime updated_at;
        private int likesCount;
        private boolean userLiked;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class BoardSimpleInfo{
        private int id;
        private int category_id;
        private int user_id;
        private String title;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class BoardDeleteDto{
        private int id;
        private int user_id;
    }
}
