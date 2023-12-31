package com.strelnikov.issuetracker.exception.exception;

public class IssueNotFoundException extends RuntimeException {

    public IssueNotFoundException(Long id) {
        super("Issue with id '" + id + "' not found");
    }
}
