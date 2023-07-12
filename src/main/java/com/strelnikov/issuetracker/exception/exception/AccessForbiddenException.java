package com.strelnikov.issuetracker.exception.exception;

public class AccessForbiddenException extends RuntimeException {

    public AccessForbiddenException() {
        super("Access denied");
    }
}
