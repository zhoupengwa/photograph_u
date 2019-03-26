package com.photograph_u.exception;

public class MyFileUploadException extends RuntimeException {
    public MyFileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyFileUploadException(String message) {
        super(message);
    }
}
