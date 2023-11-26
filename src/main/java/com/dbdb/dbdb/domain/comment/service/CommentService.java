package com.dbdb.dbdb.domain.comment.service;

import com.dbdb.dbdb.domain.comment.dto.CommentDto;
import com.dbdb.dbdb.domain.user.dto.UserDto;
import com.dbdb.dbdb.domain.user.service.UserService;
import com.dbdb.dbdb.fcm.FCMService;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.domain.comment.repository.CommentRepository;
import com.dbdb.dbdb.domain.user.repository.UserRepository;
import com.dbdb.dbdb.domain.comment.table.Comment;
import com.dbdb.dbdb.domain.comment.table.CommentLike;
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
    private final FCMService fcmService;
    private final UserService userService;

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

            boolean exists = commentRepository.findExistByCommentLike(commentLike);
            if(!exists) {
                CommentDto.CommentBoardTitleDto commentBoardTitleDto = commentRepository.getCommentWriterIdAndBoardTitle(commentLikeDto.getLiked_id());
                fcmService.sendCommentLikeMessage(userService.findUserEmailById(commentBoardTitleDto.getUser_id()), commentBoardTitleDto.getTitle());
                commentRepository.insertCommentLike(commentLike);
            } else {
                commentRepository.deleteCommentLike(commentLike);
            }

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

    public List<CommentDto.GetCommentDto> getCommentEachBoard(int writeId, int userId) {
        List<CommentDto.GetCommentDto> commentDtoList = new ArrayList<>();
        try {
            List<CommentDto.DBReturnCommentDto> commentList = commentRepository.findCommentByWriteId(writeId, userId);

            for(CommentDto.DBReturnCommentDto comment : commentList) {
                String username = "";
                UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(comment.getUser_id());
                if(userNameTypeDto.getUser_type() == 0) {
                    username = "관리자";
                } else {
                    username = userNameTypeDto.getUsername();
                }

                commentDtoList.add(new CommentDto.GetCommentDto(comment.getId(), username, comment.getWrite_id(), comment.getCategory_id(), comment.getContent(), comment.getLikeCount(), comment.isUserLiked(), comment.getCreated_at(), comment.getUpdated_at()));
            }

        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }

        return commentDtoList;
    }

    public int deleteComment(CommentDto.CommentDeleteDto commentDeleteDto) {
        try {
            UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(commentDeleteDto.getUser_id());
            if (userNameTypeDto.getUser_type() != 0) {
                int real_user = commentRepository.getCommentWriterId(commentDeleteDto.getId());
                if(real_user != commentDeleteDto.getUser_id()) {
                    throw new GlobalException(ResponseStatus.INVALID_AUTHORITY_DELETE_COMMENT);
                }
            }

            commentRepository.deleteComment(commentDeleteDto.getId());
            return commentDeleteDto.getId();

        } catch (GlobalException e) {
            throw e;
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public void modifyComment(CommentDto.CommentModifyDto commentModifyDto) {
        try {

            UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(commentModifyDto.getUser_id());
            if (userNameTypeDto.getUser_type() != 0) {
                int user_id = commentRepository.getCommentWriterId(commentModifyDto.getId());
                if(user_id != commentModifyDto.getUser_id()) {
                    throw new GlobalException(ResponseStatus.INVALID_AUTHORITY_MODIFY_COMMENT);
                }
            }

            Comment comment = new Comment(
                    commentModifyDto.getId(),
                    0,
                    0,
                    0,
                    commentModifyDto.getContent(),
                    null,
                    LocalDateTime.now()
            );

            commentRepository.modifyComment(comment);

        } catch (GlobalException e) {
            throw e;
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }
}
