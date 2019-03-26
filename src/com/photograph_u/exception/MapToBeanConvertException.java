package com.photograph_u.exception;

public class MapToBeanConvertException extends RuntimeException {
    public MapToBeanConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapToBeanConvertException(String message) {
        super(message);
    }
}
