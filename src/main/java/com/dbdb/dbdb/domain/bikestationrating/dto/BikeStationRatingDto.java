package com.dbdb.dbdb.domain.bikestationrating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BikeStationRatingDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BikeStationRatingRequest {
        private int user_id;
        private String lendplace_id1;
        private String lendplace_id2;
        private Integer rating1;
        private Integer rating2;
        private String review1;
        private String review2;

    }
}
