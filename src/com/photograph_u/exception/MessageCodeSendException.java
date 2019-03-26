package com.photograph_u.exception;

public class MessageCodeSendException extends RuntimeException {
    public MessageCodeSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageCodeSendException(String message) {
        super(message);
    }
}
