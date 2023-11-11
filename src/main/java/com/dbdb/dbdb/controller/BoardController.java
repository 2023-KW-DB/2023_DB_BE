package com.dbdb.dbdb.controller;

import com.dbdb.dbdb.dto.BoardDto;
import com.dbdb.dbdb.dto.CommentDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.service.BoardService;
import com.dbdb.dbdb.service.CommentService;
import com.dbdb.dbdb.table.BoardLike;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

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


    @GetMapping("/get-all-titles")
    public ResponseEntity<JsonResponse> allBoardTitle() {
        List<BoardDto.GetBoardTitleDto> boardTitleDtoList = boardService.getAllBoardTitle();
        if(boardTitleDtoList.isEmpty()) throw new GlobalException(ResponseStatus.RESULT_NOT_EXIST);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, boardTitleDtoList));
    }

    @GetMapping("/get-category-titles")
    public ResponseEntity<JsonResponse> eachCategoryBoardTitle(@RequestParam int category_id) {
        List<BoardDto.GetBoardTitleDto> boardTitleDtoList = boardService.getEachCategoryBoardTitle(category_id);
        if(boardTitleDtoList.isEmpty()) throw new GlobalException(ResponseStatus.RESULT_NOT_EXIST);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, boardTitleDtoList));
    }


    @GetMapping("/get-board")
    public ResponseEntity<JsonResponse> getBoard(@RequestParam int id) {
        BoardDto.GetBoardDto boardDto = boardService.getBoard(id);
        if(boardDto == null) throw new GlobalException(ResponseStatus.RESULT_NOT_EXIST);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, boardDto));
    }

    @PostMapping("/like")
    public ResponseEntity<JsonResponse> likeBoard(@RequestBody BoardDto.BoardLikeDto boardLikeDto) {
        boardService.likeBoard(boardLikeDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @DeleteMapping("/like-cancel")
    public ResponseEntity<JsonResponse> cancelLikeBoard(@RequestBody BoardDto.BoardLikeDto boardLikeDto) {
        boardService.likeCancelBoard(boardLikeDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @GetMapping("/get-comments")
    public ResponseEntity<JsonResponse> getComments(@RequestParam int write_id) {
        List<CommentDto.GetCommentDto> commentDtoList = commentService.getCommentEachBoard(write_id);
        if(commentDtoList.isEmpty()) throw new GlobalException(ResponseStatus.RESULT_NOT_EXIST);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, commentDtoList));

    }

    @PostMapping("/comment-write")
    public ResponseEntity<JsonResponse> createComment(@RequestBody CommentDto.CreateCommentDto createCommentDto) {
        commentService.createComment(createCommentDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @PostMapping("/comment/like")
    public ResponseEntity<JsonResponse> likeComment(@RequestBody CommentDto.CommentLikeDto commentLikeDto) {
        commentService.likeComment(commentLikeDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @DeleteMapping("/comment/like-cancel")
    public ResponseEntity<JsonResponse> cancelLikeComment(@RequestBody CommentDto.CommentLikeDto commentLikeDto ) {
        commentService.likeCancelComment(commentLikeDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
