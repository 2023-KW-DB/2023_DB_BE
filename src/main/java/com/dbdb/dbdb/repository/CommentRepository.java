package com.dbdb.dbdb.repository;

import com.dbdb.dbdb.table.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final JdbcTemplate jdbcTemplate;


    public void insertComment(Comment comment) {
        int user_id = comment.getUser_id();
        int write_id = comment.getWrite_id();
        int category_id = comment.getCategory_id();
        String content = comment.getContent();
        LocalDateTime created_at = comment.getCreated_at();
        LocalDateTime updated_at = comment.getUpdated_at();

        jdbcTemplate.update("INSERT INTO `comment` (`user_id`, `write_id`, `category_id`, `content`, `created_at`, `updated_at`) VALUES (?,?,?,?,?,?)",
                user_id, write_id, category_id, content, created_at, updated_at);
    }

}
