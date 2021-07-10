package com.jalin.jalinappbackend.exception;

import com.jalin.jalinappbackend.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ForbiddenExceptionHandler {
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleException(ForbiddenException exception) {
        return new ResponseEntity<>(
                new ErrorResponse(false, exception.getMessage()),
                HttpStatus.FORBIDDEN);
    }
}
