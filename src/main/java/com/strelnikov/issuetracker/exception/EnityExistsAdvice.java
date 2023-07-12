package com.strelnikov.issuetracker.exception;

import com.strelnikov.issuetracker.exception.exception.RoleAlreadyExistsException;
import com.strelnikov.issuetracker.exception.exception.UserAlreadyAssignedException;
import com.strelnikov.issuetracker.exception.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EnityExistsAdvice {

    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String UserAlreadyExistsExceptionHandler(UserAlreadyExistsException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserAlreadyAssignedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String UserAlreadyAssignedExceptionHandler(UserAlreadyAssignedException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(RoleAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String RoleAlreadyExistsExceptionHandler(RoleAlreadyExistsException exception) {
        return exception.getMessage();
    }
}
