package com.strelnikov.issuetracker.exception;

public class ProjectRoleNotFoundException extends RuntimeException {

    public ProjectRoleNotFoundException(Long id) {
        super("Project role with id '" + id + "' not found");
    }
}
