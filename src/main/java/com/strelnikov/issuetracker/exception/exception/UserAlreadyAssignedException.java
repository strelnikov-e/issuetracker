package com.strelnikov.issuetracker.exception.exception;

import com.strelnikov.issuetracker.entity.User;

public class UserAlreadyAssignedException extends RuntimeException {
    public UserAlreadyAssignedException(User user, String project) {
        super("User " + user.getFirstName() + " " + user.getLastName() + " for project " + project + " already exists");
    }
}
