package com.jalin.jalinappbackend.exception;

public class AuthenticateFailedException extends RuntimeException {
    public AuthenticateFailedException(String message) {
        super(message);
    }
}
