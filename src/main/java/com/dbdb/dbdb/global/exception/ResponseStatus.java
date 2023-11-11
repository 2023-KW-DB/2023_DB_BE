package com.dbdb.dbdb.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseStatus {
    SUCCESS(true, 200, "요청이 성공했습니다"),

    // Success
    EMAIL_DUPLICATE(true, 2000, "이메일이 중복됩니다"),
    EMAIL_NOT_DUPLICATE(true, 2001, "이메일이 중복되지 않습니다"),





    RESULT_NOT_EXIST(true, 2500, "해당하는 데이터가 존재하지 않습니다"),
    BOARD_NOT_EXIST(true, 2501, "해당하는 게시글이 존재하지 않습니다"),

    // Exception
    // 5000 - Request Error
    REQUEST_ERROR(false, 5000, "잘못된 요청입니다."),
    INVALID_REQUEST(false, 5100, "입력값을 확인해주세요"),
    UPLOAD_ERROR(false, 5200, "파일 업로드에 실패했습니다"),



    // 6000 - Response Error
    RESPONSE_ERROR(false, 6000, "값을 불러오는데 실패했습니다"),


    // 7000 - Server Connection Error
    SERVER_ERROR(false, 7000, "서버 연결에 실패했습니다."),
    DATABASE_ERROR(false, 7100, "데이터베이스 오류가 발생했습니다."),
    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;
}
