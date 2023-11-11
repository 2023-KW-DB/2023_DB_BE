package com.dbdb.dbdb.service;

import com.dbdb.dbdb.dto.BoardDto;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.repository.BoardRepository;
import com.dbdb.dbdb.table.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public void createBoard(BoardDto.CreateBoardDto createBoardDto) {
        try {
            Board board = new Board(
                    0,  //auto increment id
                    createBoardDto.getCategory_id(),
                    createBoardDto.getUser_id(),
                    0,  //views
                    createBoardDto.getTitle(),
                    createBoardDto.getContent(),
                    createBoardDto.is_notice(),
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
}
