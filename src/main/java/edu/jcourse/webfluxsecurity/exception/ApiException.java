package edu.jcourse.webfluxsecurity.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    protected final String errorCode;

    public ApiException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}