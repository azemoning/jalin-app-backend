package com.jalin.jalinappbackend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorDetailsResponse {
    private Boolean success;
    private List<ErrorDetails> errors;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ErrorDetails {
        private String fieldName;
        private String message;
    }
}
