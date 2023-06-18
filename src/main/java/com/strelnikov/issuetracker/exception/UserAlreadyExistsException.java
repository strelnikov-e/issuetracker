package com.strelnikov.issuetracker.exception;


public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String email) {
        super("User '" + email + "' already exists");
    }
}
