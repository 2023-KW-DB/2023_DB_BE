package com.dbdb.dbdb.domain.userlog.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class UserlogDto {
    private int log_id;
    private int user_id;
    private int bike_id;
    private int history_id;
    private String departure_station;
    private String arrival_station;
    private LocalDateTime departure_time;
    private LocalDateTime arrival_time;
    private long use_time;
    private int use_distance;
    private int return_status;
}
