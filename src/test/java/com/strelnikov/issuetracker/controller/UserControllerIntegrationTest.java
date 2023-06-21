package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.repository.UserRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class UserControllerIntegrationTest extends AbstractControllerTest {

    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

    @Value("Bearer ")
    private String tokenPrefix;

    @Value("Authorization")
    private String headerString;

    private String tokenForAdmin;
    private String tokenForViewer;


    @BeforeEach
    void beforeEach(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    String getTokenForAdmin() {
        if (tokenForAdmin != null) return tokenForAdmin;
        User user = new User("admin@mail.com", "password");
        final var response = restTemplate
                .postForEntity("/api/token", user, String.class);
        tokenForAdmin = response.getBody();
        return tokenForAdmin;
    }

    String getTokenForViewer() {
        if (tokenForViewer != null) return tokenForViewer;
        User user = new User("viewer@mail.com", "password");
        final var response = restTemplate
                .postForEntity("/api/token", user, String.class);
        tokenForViewer = response.getBody();
        return tokenForViewer;
    }

    @Test
    void shouldReturn401UnauthorizedUserTryingToGetUsers() {
        final var usersGetResponse =
                restTemplate.getForEntity("/api/users", ResponseEntity.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, usersGetResponse.getStatusCode());
    }

    @Test
    void shouldReturnListOfUsersSuccessfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/users",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CollectionModel.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody())
                        .getContent().toArray().length);
    }

    @Test
    void shouldReturnUserDetailsSuccessfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/users/details",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                EntityModel.class
        );
        System.out.println(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldCreateNewUserSuccessfully() {
        User user = new User("test@mail.com","password",
                true,"Joe","Doe","company");
        final var response =
                restTemplate
                        .postForEntity("/api/users", user, EntityModel.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertTrue(response.getBody().toString().contains("id=4, email=test@mail.com"));
    }

    @Test
    void shouldReturn401UnauthorizedUserTryingToUpdate() {
        User user = new User("test@mail.com","password",
                true,"Joe","Doe","company");
        HttpEntity<User> userHttpEntity = new HttpEntity<>(user);
        final var usersPostResponse =
                restTemplate
                        .exchange("/api/users?username=john", HttpMethod.PUT,
                                userHttpEntity, EntityModel.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, usersPostResponse.getStatusCode());
    }


    @Test
    void shouldUpdateUserSuccessfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());
        User user = new User("test@mail.com","password",
                true,"Joe","Doe","company");
        HttpEntity<User> body = new HttpEntity<>(user);
        System.out.println(body);
        var response = restTemplate.exchange(
                "/api/users/2",
                HttpMethod.PUT,
                new HttpEntity<>(body,headers),
                String.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        Assertions.assertTrue(Objects.requireNonNull(response.getBody()).toString().contains("id\":2,\"username\":\"john\",\"email\":\"test@mail.com\""));
    }

    @Test
    void shouldReturn401UnathorizedUserTryingToDelete() {
        final var usersDeleteResponse =
                restTemplate.exchange("/api/users?username=john", HttpMethod.DELETE,
                        HttpEntity.EMPTY, String.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, usersDeleteResponse.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestAttemptDeleteUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/users/delete",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForViewer());

        var response = restTemplate.exchange(
                "/api/users/delete",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );
        System.out.println(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
