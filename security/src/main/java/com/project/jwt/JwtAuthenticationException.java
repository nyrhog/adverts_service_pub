package com.project.jwt;

public class JwtAuthenticationException extends RuntimeException {
    public JwtAuthenticationException(String explanation) {
        super(explanation);
    }

    public JwtAuthenticationException() {
    }
}
