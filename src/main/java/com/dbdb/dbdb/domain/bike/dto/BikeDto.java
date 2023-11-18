package com.dbdb.dbdb.domain.bike.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BikeDto {
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class BikeCreateDto {
        String lendplace_id;
        int use_status;
        int bike_status;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class BikeModifyDto{
        int id;
        String lendplace_id;
        int use_status;
        int bike_status;
    }
}
