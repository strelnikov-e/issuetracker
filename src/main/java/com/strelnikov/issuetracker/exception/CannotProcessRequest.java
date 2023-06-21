package com.strelnikov.issuetracker.exception;

public class CannotProcessRequest extends RuntimeException {

    public CannotProcessRequest(String message) {
        super(message);
    }
}
