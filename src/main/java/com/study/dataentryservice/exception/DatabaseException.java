package com.study.dataentryservice.exception;

public class DatabaseException extends RuntimeException {

    private String message;

    public DatabaseException(String message) {
        super(message);
    }

    public String getMessage() {
        return message;
    }
}
