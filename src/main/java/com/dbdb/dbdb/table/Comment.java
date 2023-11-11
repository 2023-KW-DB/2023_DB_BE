package com.dbdb.dbdb.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private int id;
    private int user_id;
    private int write_id;
    private int category_id;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
