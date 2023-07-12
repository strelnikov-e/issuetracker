package com.strelnikov.issuetracker.exception.exception;


import com.strelnikov.issuetracker.entity.ProjectRoleType;

public class RoleAlreadyExistsException extends RuntimeException {

    public RoleAlreadyExistsException(ProjectRoleType role, String project) {
        super("Role '" + role + " for project " + project + "' already exists");
    }
}
