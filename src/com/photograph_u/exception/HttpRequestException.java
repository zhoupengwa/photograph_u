package com.photograph_u.exception;

public class HttpRequestException extends RuntimeException {
    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
