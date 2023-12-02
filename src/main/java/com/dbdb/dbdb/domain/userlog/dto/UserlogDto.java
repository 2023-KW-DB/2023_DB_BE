package com.dbdb.dbdb.domain.userlog.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class UserlogDto {
    private int log_id;
    private Integer user_id;
    private Integer bike_id;
    private Integer history_id;
    private String departure_station; // ST-111
    private String arrival_station;
    private LocalDateTime departure_time;
    private LocalDateTime arrival_time;
    private Long use_time;
    private Integer use_distance;
    private int return_status;
    private String depature_station_statn_addr1;
    private String depature_station_statn_addr2;
    private String arrival_station_statn_addr1;
    private String arrival_station_statn_addr2;
}
