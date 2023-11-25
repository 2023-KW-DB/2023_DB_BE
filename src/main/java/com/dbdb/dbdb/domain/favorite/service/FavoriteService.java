package com.dbdb.dbdb.domain.favorite.service;

import com.dbdb.dbdb.domain.favorite.dto.FavoriteDto;
import com.dbdb.dbdb.domain.favorite.repository.FavoriteRepository;
import com.dbdb.dbdb.domain.favorite.table.Favorite;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@RequiredArgsConstructor
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    @Transactional
    public void createFavorite(FavoriteDto.FavoriteRequestDto requestDto) {
        try {
            Favorite favorite = new Favorite(
                    requestDto.getUser_id(),
                    requestDto.getLendplace_id()
            );

            boolean exists = favoriteRepository.findByUserIdAndLendplaceId(favorite);
            if(!exists) {
                favoriteRepository.insert(favorite);
            } else {
                favoriteRepository.delete(favorite);
            }

        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }
}
