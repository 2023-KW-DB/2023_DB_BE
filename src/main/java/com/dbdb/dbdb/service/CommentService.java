package com.dbdb.dbdb.service;

import com.dbdb.dbdb.dto.CommentDto;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.repository.CommentRepository;
import com.dbdb.dbdb.table.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public void createComment(CommentDto.CreateCommentDto createCommentDto) {
        try {
            Comment comment = new Comment(
                    0, //id
                    createCommentDto.getUser_id(),
                    createCommentDto.getWrite_id(),
                    createCommentDto.getCategory_id(),
                    createCommentDto.getContent(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            commentRepository.insertComment(comment);

        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }
}
