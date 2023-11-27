package com.dbdb.dbdb.domain.favorite.repository;

import com.dbdb.dbdb.domain.favorite.dto.FavoriteDto;
import com.dbdb.dbdb.domain.favorite.table.Favorite;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<FavoriteDto.FavoriteAllDto> findAllLendplaceIdByUserId(int userId) {
        var favoriteMapper = BeanPropertyRowMapper.newInstance(FavoriteDto.FavoriteAllDto.class);

        return jdbcTemplate.query(
                "SELECT BSI.*, " +
                        "       (SELECT COUNT(*) FROM bike WHERE bike.lendplace_id = BSI.lendplace_id) AS total_bikes, " +
                        "       (SELECT COUNT(*) FROM bike WHERE bike.lendplace_id = BSI.lendplace_id AND bike.use_status = 0 AND bike.bike_status = 1) AS usable_bikes, " +
                        "       (SELECT COALESCE(AVG(rating), 0) FROM bikestationrating WHERE lendplace_id = BSI.lendplace_id) AS average_rating, " +
                        "       TRUE AS isFavorite " +
                        "FROM bikestationinformation BSI " +
                        "WHERE BSI.lendplace_id IN (SELECT lendplace_id FROM favorite WHERE user_id = ?)",
                favoriteMapper,
                userId
        );

    }
}
