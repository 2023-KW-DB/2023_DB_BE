package com.dbdb.dbdb.service;

import com.dbdb.dbdb.dto.BoardDto;
import com.dbdb.dbdb.dto.UserDto;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.repository.BoardRepository;
import com.dbdb.dbdb.repository.UserRepository;
import com.dbdb.dbdb.table.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void createBoard(BoardDto.CreateBoardDto createBoardDto) {
        try {
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
            List<Board> boardList = boardRepository.findTitleAll();

            for(Board board : boardList) {
                String username = "";
                UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(board.getUser_id());
                if(userNameTypeDto.getUser_type() == 0) {
                    username = "관리자";
                } else {
                    username = userNameTypeDto.getUsername();
                }

                boardTitleDtoList.add(new BoardDto.GetBoardTitleDto(board.getId(), board.getCategory_id(), username, board.getViews(), board.getTitle(), board.isNotice(), board.getCreated_at()));
            }

        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }

        return boardTitleDtoList;
    }

    public List<BoardDto.GetBoardTitleDto> getEachCategoryBoardTitle(int categoryId) {
        List<BoardDto.GetBoardTitleDto> boardTitleDtoList = new ArrayList<>();
        try {
            List<Board> boardList = boardRepository.findTitleByCategoryId(categoryId);
            for(Board board : boardList) {
                String username = "";
                UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(board.getUser_id());
                if(userNameTypeDto.getUser_type() == 0) {
                    username = "관리자";
                } else {
                    username = userNameTypeDto.getUsername();
                }

                boardTitleDtoList.add(new BoardDto.GetBoardTitleDto(board.getId(), board.getCategory_id(), username, board.getViews(), board.getTitle(), board.isNotice(), board.getCreated_at()));
            }

        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }

        return boardTitleDtoList;
    }

    @Transactional
    public BoardDto.GetBoardDto getBoard(int id) {
        try {
            boardRepository.increaseViewCount(id);

            Board board = boardRepository.findBoardById(id);
            String username = "";
            UserDto.UserNameTypeDto userNameTypeDto = userRepository.findNameTypeNameById(board.getUser_id());
            if (userNameTypeDto.getUser_type() == 0) {
                username = "관리자";
            } else {
                username = userNameTypeDto.getUsername();
            }
            BoardDto.GetBoardDto boardDto = new BoardDto.GetBoardDto(board.getId(), board.getCategory_id(), username, board.getViews(), board.getTitle(), board.getContent(), board.isNotice(), board.getFile_name(), board.getUrl(), board.getCreated_at(), board.getUpdated_at());

            return boardDto;
        } catch (Exception e) {
            throw new GlobalException(ResponseStatus.DATABASE_ERROR);
        }
    }

}
