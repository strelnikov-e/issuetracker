package com.strelnikov.issuetracker.exception.exception;

public class CannotProcessRequest extends RuntimeException {

    public CannotProcessRequest(String message) {
        super(message);
    }
}
