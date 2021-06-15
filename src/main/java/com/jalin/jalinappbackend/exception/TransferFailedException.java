package com.jalin.jalinappbackend.exception;


public class TransferFailedException extends RuntimeException {
    public TransferFailedException(String message) {
        super(message);
    }
}
