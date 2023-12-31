package com.dbdb.dbdb.domain.comment.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentLike {
    private int user_id;
    private int liked_id;
}
