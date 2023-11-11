package com.dbdb.dbdb.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    private int id;
    private int category_id;
    private int user_id;
    private int views;
    private String title;
    private String content;
    private boolean is_notice;
    private String file_name;
    private String url;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
