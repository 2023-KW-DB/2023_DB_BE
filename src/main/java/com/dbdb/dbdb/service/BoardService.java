package com.dbdb.dbdb.service;

import com.dbdb.dbdb.dto.BoardDto;
import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.repository.BoardRepository;
import com.dbdb.dbdb.repository.UserRepository;
import com.dbdb.dbdb.table.Board;
import com.dbdb.dbdb.table.BoardLike;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLConnection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void createBoard(BoardDto.CreateBoardDto createBoardDto) {
        try {
            UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(createBoardDto.getUser_id());
            if(userNameTypeDto.getUser_type() != 0 && createBoardDto.isNotice()) {
                throw new GlobalException(ResponseStatus.INVALID_AUTHORITY_NOTICE);
            }

            Board board = new Board(
                    0,  //auto increment id
                    createBoardDto.getCategory_id(),
                    createBoardDto.getUser_id(),
                    0,  //views
                    createBoardDto.getTitle(),
                    createBoardDto.getContent(),
                    createBoardDto.isNotice(),
                    createBoardDto.getFile_name(),
                    createBoardDto.getUrl(),
                    LocalDateTime.now(), //create at
                    LocalDateTime.now()  //update at
            );

            boardRepository.insertBoard(board);

        } catch (GlobalException e) {
            throw e;
        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }

    }

    public Map<String, String> uploadFile(MultipartFile multipartFile) {
        try {
            if (!multipartFile.isEmpty()) {
                String fileName = multipartFile.getOriginalFilename();
                String uniqueFileName = UUID.randomUUID() + "_" + fileName;

                String currentPath = System.getProperty("user.dir");
                String filePath = currentPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + uniqueFileName;

                multipartFile.transferTo(new File(filePath));

                Map<String, String> response = new HashMap<>();
                response.put("file_name", fileName);
                response.put("url", uniqueFileName);

                return response;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.UPLOAD_ERROR);
        }
    }

    public List<BoardDto.GetBoardTitleDto> getAllBoardTitle() {
        List<BoardDto.GetBoardTitleDto> boardTitleDtoList = new ArrayList<>();
        try {
            List<BoardDto.BoardWithCommentsCount> boardList = boardRepository.findTitleAll();

            for(BoardDto.BoardWithCommentsCount boardWithCommentCount : boardList) {
                String username = "";
                UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(boardWithCommentCount.getUser_id());
                if(userNameTypeDto.getUser_type() == 0) {
                    username = "관리자";
                } else {
                    username = userNameTypeDto.getUsername();
                }

                boardTitleDtoList.add(new BoardDto.GetBoardTitleDto(boardWithCommentCount.getId(), boardWithCommentCount.getCategory_id(), username, boardWithCommentCount.getViews(), boardWithCommentCount.getTitle(), boardWithCommentCount.isNotice(), boardWithCommentCount.getCreated_at(), boardWithCommentCount.getComment_count()));
            }

        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }

        return boardTitleDtoList;
    }

    public List<BoardDto.GetBoardTitleDto> getEachCategoryBoardTitle(int categoryId) {
        List<BoardDto.GetBoardTitleDto> boardTitleDtoList = new ArrayList<>();
        try {
            List<BoardDto.BoardWithCommentsCount> boardList = boardRepository.findTitleByCategoryId(categoryId);
            for(BoardDto.BoardWithCommentsCount boardWithCommentsCount : boardList) {
                String username = "";
                UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(boardWithCommentsCount.getUser_id());
                if(userNameTypeDto.getUser_type() == 0) {
                    username = "관리자";
                } else {
                    username = userNameTypeDto.getUsername();
                }

                boardTitleDtoList.add(new BoardDto.GetBoardTitleDto(boardWithCommentsCount.getId(), boardWithCommentsCount.getCategory_id(), username, boardWithCommentsCount.getViews(), boardWithCommentsCount.getTitle(), boardWithCommentsCount.isNotice(), boardWithCommentsCount.getCreated_at(), boardWithCommentsCount.getComment_count()));
            }

        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }

        return boardTitleDtoList;
    }

    @Transactional
    public BoardDto.GetBoardDto getBoard(int id, int user_id) {
        try {
            BoardDto.BoardWithLike board = boardRepository.findBoardById(id, user_id);
            if(board == null)
                return null;

            boardRepository.increaseViewCount(id);
            String username = "";
            UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(board.getUser_id());
            if (userNameTypeDto.getUser_type() == 0) {
                username = "관리자";
            } else {
                username = userNameTypeDto.getUsername();
            }
            BoardDto.GetBoardDto boardDto = new BoardDto.GetBoardDto(board.getId(), board.getCategory_id(), username, board.getViews(), board.getTitle(), board.getContent(), board.isNotice(), board.getFile_name(), board.getUrl(), board.getLikesCount(), board.isUserLiked(), board.getCreated_at(), board.getUpdated_at());

            return boardDto;
        } catch (EmptyResultDataAccessException em) {
            throw new GlobalException(ResponseStatus.RESULT_NOT_EXIST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public void likeBoard(BoardDto.BoardLikeDto boardLikeDto) {
        try {
            BoardLike boardLike = new BoardLike(
                    boardLikeDto.getUser_id(),
                    boardLikeDto.getCategory_id(),
                    boardLikeDto.getLiked_id()
            );

            boardRepository.insertBoardLike(boardLike);

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
                        throw new GlobalException(ResponseStatus.BOARD_NOT_EXIST);
                    }
                }
            } else {
                throw new GlobalException(ResponseStatus.DATABASE_ERROR);
            }
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public void likeCancelBoard(BoardDto.BoardLikeDto boardLikeDto) {
        try {
            BoardLike boardLike = new BoardLike(
                    boardLikeDto.getUser_id(),
                    boardLikeDto.getCategory_id(),
                    boardLikeDto.getLiked_id()
            );

            boardRepository.deleteBoardLike(boardLike);

        } catch (NullPointerException e) {
            throw new GlobalException(ResponseStatus.INVALID_REQUEST);
        } catch (DataIntegrityViolationException e) {
            throw new GlobalException(ResponseStatus.BOARD_NOT_EXIST);
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public Resource loadImageAsResource(String fileName) {

        try {
            //이미지 파일의 경로 구성
            String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + fileName;

            return new UrlResource("file:" + filePath); //이미지 파일 리소스 반환
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.FILE_READ_ERROR);
        }
    }

    public String getContentType(String fileName) {
        // 파일의 MIME 타입을 결정
        String contentType = URLConnection.guessContentTypeFromName(fileName);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return contentType;
    }

    public void modifyBoard(BoardDto.ModifyBoardDto modifyBoardDto) {
        try {

            UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(modifyBoardDto.getUser_id());
            if (userNameTypeDto.getUser_type() != 0) {
                if(modifyBoardDto.isNotice()) {
                    throw new GlobalException(ResponseStatus.INVALID_AUTHORITY_NOTICE);
                }

                int user_id = boardRepository.getBoardWriterId(modifyBoardDto.getId());
                if(user_id != modifyBoardDto.getUser_id()) {
                    throw new GlobalException(ResponseStatus.INVALID_AUTHORITY_MODIFY);
                }
            }

            Board board = new Board(
                    modifyBoardDto.getId(),
                    modifyBoardDto.getCategory_id(),
                    0,
                    0,
                    modifyBoardDto.getTitle(),
                    modifyBoardDto.getContent(),
                    modifyBoardDto.isNotice(),
                    modifyBoardDto.getFile_name(),
                    modifyBoardDto.getUrl(),
                    null,
                    LocalDateTime.now()
            );

            boardRepository.modifyBoard(board);

        } catch (GlobalException e) {
            throw e;
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

    public int deleteBoard(BoardDto.BoardDeleteDto boardDeleteDto) {
        try {
            UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(boardDeleteDto.getUser_id());
            if (userNameTypeDto.getUser_type() != 0) {
                int real_user = boardRepository.getBoardWriterId(boardDeleteDto.getId());
                if(real_user != boardDeleteDto.getUser_id()) {
                    throw new GlobalException(ResponseStatus.INVALID_AUTHORITY_DELETE);
                }
            }

            boardRepository.deleteBoard(boardDeleteDto.getId());
            return boardDeleteDto.getUser_id();

        } catch (GlobalException e) {
            throw e;
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }
}
