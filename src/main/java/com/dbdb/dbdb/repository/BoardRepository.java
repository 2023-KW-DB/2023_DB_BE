package com.dbdb.dbdb.repository;

import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.table.Board;
import com.dbdb.dbdb.table.BoardLike;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
        boolean notice = board.isNotice();
        String file_name = board.getFile_name();
        String url = board.getUrl();
        LocalDateTime create_at = board.getCreated_at();
        LocalDateTime update_at = board.getUpdated_at();

        jdbcTemplate.update("INSERT INTO `board` (`category_id`, `user_id`, `views`, `title`, `content`, `notice`, `file_name`, `url`, `created_at`, `updated_at`) VALUES (?,?,?,?,?,?,?,?,?,?)"
                , category_id, user_id, init_views, title, content, notice, file_name, url, create_at, update_at);
    }

    public List<Board> findTitleAll() {
        var boardMapper = BeanPropertyRowMapper.newInstance(Board.class);

        List<Board> boards = jdbcTemplate.query(
            "SELECT * FROM `board`"
                , boardMapper
        );

        return boards;
    }

    public List<Board> findTitleByCategoryId(int category_id) {
        var boardMapper = BeanPropertyRowMapper.newInstance(Board.class);

        List<Board> boards = jdbcTemplate.query(
                "SELECT * FROM `board` WHERE category_id=?"
                , boardMapper, category_id
        );

        return boards;
    }

    public Board findBoardById(int id) {
        var boardMapper = BeanPropertyRowMapper.newInstance(Board.class);

        Board board = jdbcTemplate.queryForObject(
                "SELECT * FROM `board` WHERE id=?",
                boardMapper, id
        );

        return board;
    }

    public void increaseViewCount(int id) {
        jdbcTemplate.update("UPDATE `board` SET views = views + 1 WHERE id = ?", id);
    }

    public void insertBoardLike(BoardLike boardLike) {
        int user_id = boardLike.getUser_id();
        int category_id = boardLike.getCategory_id();
        int liked_id = boardLike.getLiked_id();

        jdbcTemplate.update("INSERT INTO `board_like` (`user_id`, `category_id`, `liked_id`) VALUES (?,?,?)",
                user_id, category_id, liked_id);
    }

    public void deleteBoardLike(BoardLike boardLike) {
        int user_id = boardLike.getUser_id();
        int category_id = boardLike.getCategory_id();
        int liked_id = boardLike.getLiked_id();

        jdbcTemplate.update("DELETE FROM `board_like` WHERE user_id=? AND category_id=? AND liked_id=?",
                user_id, category_id, liked_id);
    }
}
