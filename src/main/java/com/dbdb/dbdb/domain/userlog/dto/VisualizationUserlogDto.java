package com.dbdb.dbdb.domain.userlog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class VisualizationUserlogDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class userUseTimeInfo {
        private int id;
        private String username;
        private int user_type;
        private String email;
        private String phone_number;
        private double weight;
        private int age;
        private long total_use_time;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class userUseCountInfo {
        private int id;
        private String username;
        private int user_type;
        private String email;
        private String phone_number;
        private double weight;
        private int age;
        private long total_use_count;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class userUseDistanceInfo {
        private int id;
        private String username;
        private int user_type;
        private String email;
        private String phone_number;
        private double weight;
        private int age;
        private long total_use_distance;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class userLogDto {
        private int log_id;
        private Integer user_id;
        private Integer bike_id;
        private Integer history_id;
        private String departure_station;
        private String arrival_station;
        private LocalDateTime departure_time;
        private LocalDateTime arrival_time;
        private long use_time;
        private int use_distance;
        private int return_status;
    }
}
