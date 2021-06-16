package com.project.exception;

public class RateException extends RuntimeException{
    public RateException() {
    }

    public RateException(String message) {
        super(message);
    }

    public RateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RateException(Throwable cause) {
        super(cause);
    }
}
