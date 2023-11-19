package com.dbdb.dbdb.domain.comment.repository;

import com.dbdb.dbdb.domain.comment.dto.CommentDto;
import com.dbdb.dbdb.domain.comment.table.Comment;
import com.dbdb.dbdb.domain.comment.table.CommentLike;
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

    public boolean findExistByCommentLike(CommentLike commentLike) {
        Boolean isExists = jdbcTemplate.queryForObject(
                "SELECT EXISTS ( " +
                        "    SELECT 1 FROM comment_like " +
                        "    WHERE user_id = ? AND liked_id = ? " +
                        ") ", Boolean.class, commentLike.getUser_id(), commentLike.getLiked_id());

        return isExists;
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

    public List<CommentDto.DBReturnCommentDto> findCommentByWriteId(int writeId, int userId) {
        var commentMapper = BeanPropertyRowMapper.newInstance(CommentDto.DBReturnCommentDto.class);

        List<CommentDto.DBReturnCommentDto> comments = jdbcTemplate.query(
                "SELECT C.*, " +
                        "(SELECT COUNT(*) FROM `comment_like` WHERE `liked_id` = C.id) AS `like_count`, " +
                        "EXISTS(SELECT 1 FROM `comment_like` WHERE `liked_id` = C.id AND `user_id` = ?) AS `user_liked` " +
                        "FROM `comment` C WHERE C.`write_id` = ?",
                commentMapper, userId, writeId
        );

        return comments;
    }

    public Integer getCommentWriterId(int commentId) {
        return jdbcTemplate.queryForObject(
                "SELECT user_id FROM comment WHERE id=?",
                Integer.class,
                commentId
        );
    }

    public void deleteComment(int id) {
        jdbcTemplate.update("DELETE FROM `comment` WHERE id=?",
                id);
    }

    public void modifyComment(Comment comment) {
        int id = comment.getId();
        String content = comment.getContent();
        LocalDateTime update_at = comment.getUpdated_at();

        jdbcTemplate.update("UPDATE `comment` SET " +
                        "content=?, " +
                        "updated_at=? " +
                        "WHERE id=?"
                , content, update_at, id);
    }

}
