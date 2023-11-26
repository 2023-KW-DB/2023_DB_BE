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
    SUCCESS_LOGOUT(true, 2003, "로그아웃을 성공하였습니다."),
    SUCCESS_SEND_AUTHCODE(true, 2004, "이메일 인증번호 전송을 성공하였습니다"),
    SUCCESS_CORRECT_AUTHCODE(true, 2005, "인증번호가 일치합니다."),
    SUCCESS_NOT_CORRECT_AUTHCODE(true, 2006, "인증번호가 일치하지 않습니다."),
    SUCCESS_CHANGE_PASSWORD(true, 2007, "비밀번호 변경을 성공하였습니다."),
    SUCCESS_WITHDRAWAL(true, 2008, "회원탈퇴를 성공하였습니다"),
    SUCCESS_GET_ALL_USERS_INFO(true, 2009, "[관리자] 모든 유저의 정보가 반환되었습니다."),
    SUCCESS_GET_ALL_USERS_INFO_ISEMPTY(true, 2010, "[관리자] 저장된 유저의 정보가 하나도 없습니다."),
    SUCCESS_MODIFY_USER_INFO(true, 2011, "[관리자] 유저 정보 수정을 성공하였습니다."),
    SUCCESS_DELETE_USER(true, 2012, "[관리자] 해당 유저의 정보를 삭제(탈퇴)를 성공하였습니다"),
    SUCCESS_KAKAO_LOGIN(true, 2013, "카카오 로그인을 성공하였습니다."),
    SUCCESS_FIND_USER_BY_ID(true, 2014, "id에 해당하는 유저 정보 반환에 성공했습니다."),
    SUCCESS_NOT_FIND_USER_BY_ID(true, 2015, "id에 해당하는 유저 정보가 없습니다."),
    SUCCESS_SIGNUP(true, 2016, "회원 가입에 성공했습니다"),
    SUCCESS_NOT_SIGNUP(true, 2017, "해당 이메일로 가입이 되어 있는 회원입니다."),
    SUCCESS_CREATE_TICKET(true, 2018, "[관리자] 새로운 가격의 이용권을 추가에 성공하였습니다."),
    SUCCESS_MODIFY_TICKET(true, 2019, "[관리자] 해당 이용권의 가격을 수정에 성공하였습니다."),
    SUCCESS_DELETE_TICKET(true, 2020, "[관리자] 해당 이용권의 종류를 삭제에 성공하였습니다."),
    SUCCESS_GET_ALL_TICKETS_INFO(true, 2021, "모든 티켓의 정보 반환에 성공하였습니다."),
    SUCCESS_GET_ALL_TICKETS_INFO_ISEMPTY(true, 2022, "저장된 티켓의 정보가 하나도 없습니다."),
    SUCCESS_PURCHASE_TICKET(true, 2023, "이용권 구매에 성공하였습니다."),
    SUCCESS_GET_ALL_PAYMENT_HISTORY_INFO(true, 2024, "해당 유저의 모든 티켓 구매 내역 정보 반환에 성공하였습니다."),
    SUCCESS_GET_ALL_PAYMENT_HISTORY_INFO_ISEMPTY(true, 2025, "해당 유저의 티켓 구매 내역 정보가 하나도 없습니다."),
    SUCCESS_BIKE_RENTAL(true, 2026, "자전거 대여에 성공하였습니다."),
    SUCCESS_BIKE_RETURN(true ,2027, "자전거 반납에 성공하였습니다."),
    SUCCESS_GET_USERLOG(true, 2028, "해당 유저의 대여 기록(userlog) 조회에 성공하였습니다."),
    SUCCESS_GET_USERLOG_EMPTY(true, 2029, "해당 유저의 대여 기록(userlog) 조회에 성공하였으나, 아무 기록도 존재하지 않습니다."),
    SUCCESS_GET_ALL_USERLOG(true, 2030, "모든 유저의 모든 대여 기록(userlog) 조회에 성공하였습니다."),
    SUCCESS_GET_ALL_USERLOG_EMPTY(true, 2031, "모든 유저의 모든 대여 기록(userlog) 조회에 성공하였으나, 아무 기록도 존재하지 않습니다."),
    SUCCESS_CREATE_COUPON(true, 2032, "[관리자] 새로운 쿠폰 생성을 성공하였습니다."),
    SUCCESS_MODIFY_COUPON(true, 2033, "[관리자] 해당 쿠폰 정보 변경을 성공하였습니다."),
    SUCCESS_DELETE_COUPON(true, 2034, "[관리자] 해당 쿠폰에 대해 삭제를 성공하였습니다."),
    SUCCESS_GET_ALL_COUPON(true, 2035, "[관리자] 모든 쿠폰 조회를 성공하였습니다."),
    SUCCESS_GET_ALL_COUPON_EMPTY(true, 2036, "[관리자] 모든 쿠폰 조회를 성공하였으나, 아무 쿠폰도 존재하지 않습니다."),



    RESULT_NOT_EXIST(true, 2500, "해당하는 데이터가 존재하지 않습니다"),
    BOARD_NOT_EXIST(true, 2501, "해당하는 게시글이 존재하지 않습니다"),
    COMMENT_NOT_EXIST(true, 2502, "해당하는 댓글이 존재하지 않습니다"),

    // Exception
    // 5000 - Request Error
    REQUEST_ERROR(false, 5000, "잘못된 요청입니다."),
    ERROR_LOGIN(false, 5001, "로그인을 실패하였습니다. 이메일 혹은 비밀번호를 다시 확인해주세요."),
    ERROR_TIMEOVER_AUTHCODE(false, 5002, "5분이 지나 해당 인증 번호는 만료되었습니다."),
    INVALID_REQUEST(false, 5003, "입력값을 확인해주세요"),
    UPLOAD_ERROR(false, 5004, "파일 업로드에 실패했습니다."),
    COOKIE_ERROR(false, 5005, "존재하지 않는 쿠키입니다."),
    SESSION_ERROR(false, 5006, "존재하지 않는 세션입니다."),
    ACCESS_TOKEN_NULL(false, 5007, "존재하지 않는 액세스 토큰입니다"),
    FAILED_NOT_ENOUGHT_TOTAL_MONEY(false, 5008, "해당 유저의 잔고(total_money)가 충분하지 않아 이용권 구매에 실패하였습니다."),
    FAILED_INVALID_BIKE(false, 5009, "해당 대여소에서는 이용 가능한 자전거가 없습니다.(bike_status=0)"),
    FAILED_INVALID_RENTAL_STATION(false, 5010, "해당 대여소에서는 대여가 불가능한 상태입니다.(station_status=0)"),
    FAILED_INVALID_RETURN_STATION(false, 5011, "해당 대여소에서는 반납이 불가능한 상태입니다.(station_status=0)"),
    FAILED_NO_VALID_TICKET(false, 5012, "사용 가능한 이용권이 없어 대여가 불가능한 상태입니다."),
    FAILED_OVER_MAX_STANDS(false, 5013, "해당 대여소의 최대 거치 가능 자전거 수를 초과하였습니다."),
    FILE_READ_ERROR(false, 5201, "파일을 조회할 때 오류가 발생했습니다."),
    INVALID_AUTHORITY_NOTICE(false, 5202, "관리자만 공지를 작성할 수 있습니다"),
    INVALID_AUTHORITY_MODIFY(false, 5203, "게시글은 작성자만 수정할 수 있습니다"),
    INVALID_AUTHORITY_DELETE(false, 5204, "게시글은 작성자만 삭제할 수 있습니다"),
    INVALID_AUTHORITY_MODIFY_COMMENT(false, 5205, "댓글은 작성자만 수정할 수 있습니다"),
    INVALID_AUTHORITY_DELETE_COMMENT(false, 5206, "댓글은 작성자만 삭제할 수 있습니다"),
    FK_VIOLATION_STATION(false, 5207, "존재하지 않는 대여소입니다"),
    DUPLICATE_STATION(false, 5208, "이미 존재하는 대여소 id입니다"),


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
