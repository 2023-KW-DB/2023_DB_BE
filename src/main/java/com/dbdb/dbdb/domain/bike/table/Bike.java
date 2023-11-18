package com.dbdb.dbdb.domain.bike.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bike {
    int id;
    String lendplace_id;
    int use_status;
    int bike_status;
}
