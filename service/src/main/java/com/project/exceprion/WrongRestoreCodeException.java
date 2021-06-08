package com.project.exceprion;

public class WrongRestoreCodeException extends RuntimeException{
    public WrongRestoreCodeException() {
    }

    public WrongRestoreCodeException(String message) {
        super(message);
    }

    public WrongRestoreCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongRestoreCodeException(Throwable cause) {
        super(cause);
    }
}
