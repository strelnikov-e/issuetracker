package com.strelnikov.issuetracker.exception;

import com.strelnikov.issuetracker.exception.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EntityNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String ProjectNotFoundHandler(UserNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String ProjectNotFoundHandler(ProjectNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(IssueNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String IssueNotFoundHandler(IssueNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(TagNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String TagNotFoundHandler(TagNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AccessForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String AccessForbiddenHandler(AccessForbiddenException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ProjectRoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String RoleNotFoundException(ProjectRoleNotFoundException exception) {
        return exception.getMessage();
    }
}
