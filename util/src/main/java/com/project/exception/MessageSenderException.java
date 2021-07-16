package com.project.exception;

public class MessageSenderException extends RuntimeException{

    public MessageSenderException() {
    }

    public MessageSenderException(String message) {
        super(message);
    }

    public MessageSenderException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageSenderException(Throwable cause) {
        super(cause);
    }
}
