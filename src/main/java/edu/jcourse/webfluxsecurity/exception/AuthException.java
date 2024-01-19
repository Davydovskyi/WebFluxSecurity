package edu.jcourse.webfluxsecurity.exception;

public class AuthException extends ApiException {
    public AuthException(String errorCode, String message) {
        super(errorCode, message);
    }
}