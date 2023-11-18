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

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class BikeDeleteDto {
        int id;
    }

    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class BikeDetailDto{
        int id;
        String lendplace_id;
        int use_status;
        int bike_status;
        String statn_addr1;
        String statn_addr2;
        Double startn_lat;
        Double startn_lnt;
    }
}
