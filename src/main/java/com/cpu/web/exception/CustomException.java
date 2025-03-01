package com.cpu.web.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{
    private final HttpStatus status;

    public CustomException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }
}
