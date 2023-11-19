package com.dbdb.dbdb.domain.favorite.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    int user_id;
    String lendplace_id;
}
