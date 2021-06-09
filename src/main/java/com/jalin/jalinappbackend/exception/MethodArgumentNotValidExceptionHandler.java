package com.jalin.jalinappbackend.exception;

import com.jalin.jalinappbackend.model.ErrorDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException exception) {
        List<FieldError> errors = exception.getFieldErrors();
        List<ErrorDetailsResponse.ErrorDetails> errorDetails = new ArrayList<>();
        for (FieldError fieldError : errors) {
            ErrorDetailsResponse.ErrorDetails error = new ErrorDetailsResponse.ErrorDetails(
                    fieldError.getField(),
                    fieldError.getDefaultMessage());
            errorDetails.add(error);
        }
        return new ResponseEntity<>(
                new ErrorDetailsResponse(false, errorDetails),
                HttpStatus.BAD_REQUEST);
    }
}
