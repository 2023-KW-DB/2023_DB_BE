package com.dbdb.dbdb.domain.bikestation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
