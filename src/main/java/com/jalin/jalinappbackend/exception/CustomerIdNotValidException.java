package com.jalin.jalinappbackend.exception;

public class CustomerIdNotValidException extends RuntimeException {
    public CustomerIdNotValidException(String message) {
        super(message);
    }
}
