package com.dbdb.dbdb.domain.favorite.controller;

import com.dbdb.dbdb.domain.favorite.dto.FavoriteDto;
import com.dbdb.dbdb.domain.favorite.service.FavoriteService;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/favorite-lendplace")
    public ResponseEntity<JsonResponse> createBoard(@RequestBody FavoriteDto.FavoriteRequestDto requestDto) {
        favoriteService.createFavorite(requestDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
