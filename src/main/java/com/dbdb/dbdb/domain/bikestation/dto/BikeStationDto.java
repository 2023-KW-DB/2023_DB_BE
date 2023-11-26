package com.dbdb.dbdb.domain.bikestation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BikeStationDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BikeStationDetailDto {
        String lendplace_id;
        String statn_addr1;
        String statn_addr2;
        double startn_lat;
        double startn_lnt;
        Double max_stands;
        Integer station_status;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BikeStationDeleteDto {
        String lendplace_id;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BikeStationWithBikeDto {
        String lendplace_id;
        String statn_addr1;
        String statn_addr2;
        double startn_lat;
        double startn_lnt;
        Double max_stands;
        Integer station_status;
        int total_bikes;
        int usable_bikes;
        int empty_stands;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BikeStationStatus {
        String lendplace_id;
        String statn_addr1;
        String statn_addr2;
        int usable_bikes;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BikeStationWithCurrentBike {
        String lendplace_id;
        String statn_addr1;
        String statn_addr2;
        double startn_lat;
        double startn_lnt;
        Double max_stands;
        Integer station_status;
        int total_bikes;
        int usable_bikes;
        double average_rating;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BikeStationSimpleState {
        String lendplace_id;
        String statn_addr1;
        String statn_addr2;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BikeStationSimpleWithState {
        String lendplace_id;
        String statn_addr1;
        String statn_addr2;
        LocalDateTime time;
    }

}
