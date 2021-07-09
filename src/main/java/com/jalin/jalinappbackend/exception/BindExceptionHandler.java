package com.jalin.jalinappbackend.exception;

import com.jalin.jalinappbackend.model.ErrorDetails;
import com.jalin.jalinappbackend.model.ErrorDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class BindExceptionHandler {
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleException(BindException exception) {
        List<FieldError> errors = exception.getFieldErrors();
        List<ErrorDetails> errorDetails = new ArrayList<>();
        for (FieldError fieldError : errors) {
            ErrorDetails error = new ErrorDetails(
                    fieldError.getField(),
                    fieldError.getDefaultMessage());
            errorDetails.add(error);
        }
        return new ResponseEntity<>(
                new ErrorDetailsResponse(false, "One or more errors occurred", errorDetails),
                HttpStatus.BAD_REQUEST);
    }
}
