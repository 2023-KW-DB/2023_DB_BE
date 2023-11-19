package com.dbdb.dbdb.domain.favorite.repository;

import com.dbdb.dbdb.domain.favorite.dto.FavoriteDto;
import com.dbdb.dbdb.domain.favorite.table.Favorite;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FavoriteRepository {
    private final JdbcTemplate jdbcTemplate;

    public boolean findByUserIdAndLendplaceId(Favorite favorite) {
        Boolean isExists = jdbcTemplate.queryForObject(
                "SELECT EXISTS ( " +
                "    SELECT 1 FROM favorite " +
                "    WHERE user_id = ? AND lendplace_id = ? " +
                ") ", Boolean.class, favorite.getUser_id(), favorite.getLendplace_id());

        return isExists;
    }

    public void insert(Favorite favorite) {
        jdbcTemplate.update("INSERT INTO `favorite` (`user_id`, `lendplace_id`) VALUES (?,?)",
                favorite.getUser_id(), favorite.getLendplace_id());
    }

    public void delete(Favorite favorite) {
        jdbcTemplate.update("DELETE FROM `favorite` WHERE user_id=? AND lendplace_id=?",
                favorite.getUser_id(), favorite.getLendplace_id());
    }
}
