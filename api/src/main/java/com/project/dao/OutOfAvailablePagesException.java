package com.project.dao;

public class OutOfAvailablePagesException extends RuntimeException{
    public OutOfAvailablePagesException() {
    }

    public OutOfAvailablePagesException(String message) {
        super(message);
    }

    public OutOfAvailablePagesException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutOfAvailablePagesException(Throwable cause) {
        super(cause);
    }
}
