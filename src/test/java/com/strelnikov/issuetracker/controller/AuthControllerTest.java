package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuthControllerTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void tokenShouldReturn401UnauthorizedPostWrongCredentials() {
        User user = new User("admin@mail.com", "wrongPassword");
        final var response = restTemplate
                .postForEntity("/api/token", user,ResponseEntity.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturnTokenSuccessfully() {
        User user = new User("admin@mail.com", "password");
        final var response = restTemplate
                .postForEntity("/api/token", user, String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
