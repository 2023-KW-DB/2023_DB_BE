package com.dbdb.dbdb.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BadRequestException extends RuntimeException{
    private ResponseStatus status;
}
