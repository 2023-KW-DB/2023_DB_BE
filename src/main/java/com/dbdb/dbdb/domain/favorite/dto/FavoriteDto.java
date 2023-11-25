package com.dbdb.dbdb.domain.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FavoriteDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class FavoriteRequestDto {
        int user_id;
        String lendplace_id;
    }



}
