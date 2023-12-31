package com.dbdb.dbdb.domain.board.controller;

import com.dbdb.dbdb.domain.board.dto.BoardDto;
import com.dbdb.dbdb.domain.comment.dto.CommentDto;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.GlobalException;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import com.dbdb.dbdb.domain.board.service.BoardService;
import com.dbdb.dbdb.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, boardTitleDtoList));
    }

    @GetMapping("/get-category-titles")
    public ResponseEntity<JsonResponse> eachCategoryBoardTitle(@RequestParam int category_id) {
        List<BoardDto.GetBoardTitleDto> boardTitleDtoList = boardService.getEachCategoryBoardTitle(category_id);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, boardTitleDtoList));
    }

    @GetMapping("/get-notice-titles")
    public ResponseEntity<JsonResponse> eachNoticeBoardTitle() {
        List<BoardDto.GetBoardTitleDto> boardTitleDtoList = boardService.getNoticeBoardTitle();
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, boardTitleDtoList));
    }

    @GetMapping("/get-board")
    public ResponseEntity<JsonResponse> getBoard(@RequestParam int id, @RequestParam int user_id) {
        BoardDto.GetBoardDto boardDto = boardService.getBoard(id, user_id);
        if(boardDto == null) throw new GlobalException(ResponseStatus.RESULT_NOT_EXIST);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, boardDto));
    }

    @PatchMapping("/modify-board")
    private ResponseEntity<JsonResponse> modifyBoard(@RequestBody BoardDto.ModifyBoardDto modifyBoardDto) {

        boardService.modifyBoard(modifyBoardDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @DeleteMapping("/delete-board")
    private ResponseEntity<JsonResponse> deleteBoard(@RequestBody BoardDto.BoardDeleteDto boardDeleteDto) {

        int board_id = boardService.deleteBoard(boardDeleteDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, board_id));
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
    public ResponseEntity<JsonResponse> getComments(@RequestParam int write_id, @RequestParam int user_id) {
        List<CommentDto.GetCommentDto> commentDtoList = commentService.getCommentEachBoard(write_id, user_id);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, commentDtoList));

    }

    @PostMapping("/comment-write")
    public ResponseEntity<JsonResponse> createComment(@RequestBody CommentDto.CreateCommentDto createCommentDto) {
        commentService.createComment(createCommentDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @PatchMapping("/comment-modify")
    public ResponseEntity<JsonResponse> modifyComment(@RequestBody CommentDto.CommentModifyDto commentModifyDto) {
        commentService.modifyComment(commentModifyDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @DeleteMapping("/comment-delete")
    public ResponseEntity<JsonResponse> deleteComment(@RequestBody CommentDto.CommentDeleteDto commentDeleteDto) {
        int id = commentService.deleteComment(commentDeleteDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, id));
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


    @GetMapping(path = "/{fileName}") //이미지 파일을 반환하는 handler
    public ResponseEntity<Resource> returnImage(@PathVariable String fileName) {

        Resource resource = boardService.loadImageAsResource(fileName);
        String contentType = boardService.getContentType(fileName);

        if (!resource.exists() || !resource.isReadable()) {
            throw new GlobalException(ResponseStatus.RESPONSE_ERROR);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
