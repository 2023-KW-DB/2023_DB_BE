package com.dbdb.dbdb.service;

import com.dbdb.dbdb.dto.CommentDto;
import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.repository.CommentRepository;
import com.dbdb.dbdb.repository.UserRepository;
import com.dbdb.dbdb.table.BoardLike;
import com.dbdb.dbdb.table.Comment;
import com.dbdb.dbdb.table.CommentLike;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

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
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(ResponseStatus.BOARD_NOT_EXIST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public void likeComment(CommentDto.CommentLikeDto commentLikeDto) {
        try {
            CommentLike commentLike = new CommentLike(
                    commentLikeDto.getUser_id(),
                    commentLikeDto.getLiked_id()
            );

            commentRepository.insertCommentLike(commentLike);

        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (DataIntegrityViolationException e) {
            Throwable rootCause = e.getRootCause();
            if (rootCause instanceof SQLException) {
                SQLException sqlEx = (SQLException) rootCause;
                String sqlState = sqlEx.getSQLState();

                if ("23000".equals(sqlState)) {
                    String message = sqlEx.getMessage();
                    if (message.contains("Duplicate entry")) {
                        return;
                    } else if (message.contains("a foreign key constraint fails")) {
                        throw new GlobalException(ResponseStatus.COMMENT_NOT_EXIST);
                    }
                }
            } else {
                throw new GlobalException(ResponseStatus.DATABASE_ERROR);
            }
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public void likeCancelComment(CommentDto.CommentLikeDto commentLikeDto) {
        try {
            CommentLike commentLike = new CommentLike(
                    commentLikeDto.getUser_id(),
                    commentLikeDto.getLiked_id()
            );

            commentRepository.deleteCommentLike(commentLike);

        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(ResponseStatus.COMMENT_NOT_EXIST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public List<CommentDto.GetCommentDto> getCommentEachBoard(int writeId) {
        List<CommentDto.GetCommentDto> commentDtoList = new ArrayList<>();
        try {
            List<Comment> commentList = commentRepository.findCommentByWriteId(writeId);

            for(Comment comment : commentList) {
                String username = "";
                UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(comment.getUser_id());
                if(userNameTypeDto.getUser_type() == 0) {
                    username = "관리자";
                } else {
                    username = userNameTypeDto.getUsername();
                }

                commentDtoList.add(new CommentDto.GetCommentDto(comment.getId(), username, comment.getWrite_id(), comment.getCategory_id(), comment.getContent(), comment.getCreated_at(), comment.getUpdated_at()));
            }

        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }

        return commentDtoList;
    }
}
