package com.dbdb.dbdb.repository;

import com.dbdb.dbdb.table.Board;
import com.dbdb.dbdb.table.Comment;
import com.dbdb.dbdb.table.CommentLike;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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

    public void insertCommentLike(CommentLike commentLike) {
        int user_id = commentLike.getUser_id();
        int liked_id = commentLike.getLiked_id();

        jdbcTemplate.update("INSERT INTO `comment_like` (`user_id`, `liked_id`) VALUES (?,?)",
                user_id, liked_id);
    }

    public void deleteCommentLike(CommentLike commentLike) {
        int user_id = commentLike.getUser_id();
        int liked_id = commentLike.getLiked_id();

        jdbcTemplate.update("DELETE FROM `comment_like` WHERE user_id=? AND liked_id=?",
                user_id, liked_id);
    }

    public List<Comment> findCommentByWriteId(int writeId) {
        var commentMapper = BeanPropertyRowMapper.newInstance(Comment.class);

        List<Comment> comments = jdbcTemplate.query(
                "SELECT * FROM `comment` WHERE write_id=?"
                , commentMapper, writeId
        );

        return comments;
    }
}
