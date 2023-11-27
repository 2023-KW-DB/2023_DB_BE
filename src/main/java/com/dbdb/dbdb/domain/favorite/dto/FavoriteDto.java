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

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class FavoriteAllDto {
        String lendplace_id;
        String statn_addr1;
        String statn_addr2;
        double startn_lat;
        double startn_lnt;
        Double max_stands;
        Integer station_status;
        int total_bikes;
        int usable_bikes;
        Boolean isFavorite;
        double average_rating;
    }

}
