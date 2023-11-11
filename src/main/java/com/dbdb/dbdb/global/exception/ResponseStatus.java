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
    SUCCESS_LOGIN(true, 2002, "로그인을 성공하였습니다."),
    SUCCESS_SEND_AUTHCODE(true, 2003, "이메일 인증번호 전송을 성공하였습니다"),





    // Exception
    // 5000 - Request Error
    REQUEST_ERROR(false, 5000, "잘못된 요청입니다."),
    LOGIN_ERROR(false, 5001, "로그인을 실패하였습니다. 이메일 혹은 비밀번호를 다시 확인해주세요."),
    INVALID_REQUEST(false, 5100, "입력값을 확인해주세요"),
    UPLOAD_ERROR(false, 5200, "파일 업로드에 실패했습니다"),



    // 6000 - Response Error
    RESPONSE_ERROR(false, 6000, "값을 불러오는데 실패했습니다"),


    // 7000 - Server Connection Error
    SERVER_ERROR(false, 7000, "서버 연결에 실패했습니다."),
    DATABASE_ERROR(false, 7100, "데이터베이스 연결에 실패했습니다."),
    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;
}
