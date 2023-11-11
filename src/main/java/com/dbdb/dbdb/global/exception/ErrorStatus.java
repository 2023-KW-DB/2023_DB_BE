package com.dbdb.dbdb.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorStatus {

    // 2000 - Request Error
    REQUEST_ERROR(false, 2000, "잘못된 요청입니다."),
    INVALID_REQUEST(false, 2100, "입력값을 확인해주세요"),



    // 3000 - Response Error
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패했습니다"),


    // 5000 - Server Connection Error
    SERVER_ERROR(false, 5000, "서버 연결에 실패했습니다."),
    DATABASE_ERROR(false, 5100, "데이터베이스 연결에 실패했습니다."),
    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;
}
