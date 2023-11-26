package com.dbdb.dbdb.domain.bikestationrating.table;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BikeStationRating {
    String lendplace_id;
    int user_id;
    int rating;
    String review;
}
