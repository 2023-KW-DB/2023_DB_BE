package com.dbdb.dbdb.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardLike {
    private int user_id;
    private int category_id;
    private int liked_id;
}
