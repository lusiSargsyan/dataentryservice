package com.study.dataentryservice.exception;

import lombok.Getter;

public class ValidationException extends RuntimeException {

    @Getter
    private final String message;

    public ValidationException(String message) {
        this.message = message;
    }
}
