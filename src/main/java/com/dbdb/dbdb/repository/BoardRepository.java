package com.dbdb.dbdb.repository;

import com.dbdb.dbdb.table.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
    private final JdbcTemplate jdbcTemplate;

    public void insertBoard(Board board) {
        int category_id = board.getCategory_id();
        int user_id = board.getUser_id();
        int init_views = 0;
        String title = board.getTitle();
        String content = board.getContent();
        boolean is_notice = board.is_notice();
        String file_name = board.getFile_name();
        String url = board.getUrl();
        LocalDateTime create_at = board.getCreated_at();
        LocalDateTime update_at = board.getUpdated_at();

        jdbcTemplate.update("INSERT INTO `board` (`category_id`, `user_id`, `views`, `title`, `content`, `is_notice`, `file_name`, `url`, `created_at`, `updated_at`) VALUES (?,?,?,?,?,?,?,?,?,?)"
                , category_id, user_id, init_views, title, content, is_notice, file_name, url, create_at, update_at);
    }


}
