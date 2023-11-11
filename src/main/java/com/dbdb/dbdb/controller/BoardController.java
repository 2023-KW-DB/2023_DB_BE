package com.dbdb.dbdb.controller;

import com.dbdb.dbdb.dto.BoardDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/create-post")
    public ResponseEntity<JsonResponse> createBoard(@RequestBody BoardDto.CreateBoardDto createBoardDto) {
        boardService.createBoard(createBoardDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @PostMapping("/file-upload")
    public ResponseEntity<JsonResponse<Map<String, String>>> uploadFile(@RequestParam("file") MultipartFile multipartFile) {

        Map<String, String> response = new HashMap<>();
        response = boardService.uploadFile(multipartFile);

        return ResponseEntity.ok(new JsonResponse<Map<String, String>>(ResponseStatus.SUCCESS, response));
    }



}
