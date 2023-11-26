package com.dbdb.dbdb.domain.favorite.controller;

import com.dbdb.dbdb.domain.favorite.dto.FavoriteDto;
import com.dbdb.dbdb.domain.favorite.service.FavoriteService;
import com.dbdb.dbdb.global.dto.JsonResponse;
import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/favorite-lendplace")
    public ResponseEntity<JsonResponse> favoriteLendplace(@RequestBody FavoriteDto.FavoriteRequestDto requestDto) {
        favoriteService.createFavorite(requestDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @GetMapping("/get-lendplace")
    public ResponseEntity<JsonResponse> getFavoriteLendplace(@RequestParam int user_id) {
        List<FavoriteDto.FavoriteAllDto> favoriteAllDtoList = favoriteService.findAllLendplace(user_id);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, favoriteAllDtoList));
    }
}
