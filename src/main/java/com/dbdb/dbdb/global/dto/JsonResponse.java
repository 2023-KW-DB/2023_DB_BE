package com.dbdb.dbdb.global.dto;

import com.dbdb.dbdb.global.exception.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.dbdb.dbdb.global.exception.ResponseStatus.SUCCESS;

@Data
public class JsonResponse <T> {
    private final String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private boolean isSuccess;
    private int status;
    private String message;
    private Object result;

    // 요청 성공
    public JsonResponse(ResponseStatus status, T result){
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.status = status.getCode();
        this.result = result;
    }

    // 요청 실패
    public JsonResponse(ResponseStatus status){
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.status = status.getCode();
        this.result = null;
    }
}
