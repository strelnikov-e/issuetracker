package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.Tag;
import com.strelnikov.issuetracker.entity.User;
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

public class TagControllerTest extends AbstractControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

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
    void shouldReturn401UnathorizedUserTryingToGetTags() {
        final var response =
                restTemplate.getForEntity("/api/tags", ResponseEntity.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturn403ForbiddenUserTryingToGetTagsForIssue() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForViewer());

        var response = restTemplate.exchange(
                "/api/issues/5/tags",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CollectionModel.class
        );
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void shouldGetListOfTagsSuccessfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/issues/1/tags",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CollectionModel.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(3,response.getBody().getContent().size());
    }

    @Test
    void shouldGetTagSuccesfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/tags/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                EntityModel.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().getContent().toString().contains("id=1, name=database"));
    }

    @Test
    void shouldReturn401IfUnauthorizedUserTryingToCreateTagForIssue() {
        Tag tag = new Tag("TestTag");
        final var projectCreatedResponse =
                restTemplate.postForEntity(
                        "/api/issues/1/tags",
                        tag,
                        ResponseEntity.class
                );
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, projectCreatedResponse.getStatusCode());
    }

    @Test
    void shouldCreateTagForIssueSuccesfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());
        Tag tag = new Tag("TestTag");

        var response = restTemplate.exchange(
                "/api/issues/1/tags",
                HttpMethod.POST,
                new HttpEntity<>(tag ,headers),
                EntityModel.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertTrue(response.getBody().getContent().toString().contains("TestTag"));
    }

    @Test
    void shouldReturn403ForbiddenTryingToCreateTagForIssueByUnauthorizedUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForViewer());
        Tag tag = new Tag("TestTag");

        var response = restTemplate.exchange(
                "/api/issues/2/tags",
                HttpMethod.POST,
                new HttpEntity<>(tag ,headers),
                EntityModel.class
        );
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


    @Test
    void shouldReturn403ForbiddenTryingDeleteIssueByUnauthorizedUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForViewer());
        Tag tag = new Tag("TestTag");

        var response = restTemplate.exchange(
                "/api/issues/5/tags/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                EntityModel.class
        );
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void shouldDeleteIssueSuccessfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());
        Tag tag = new Tag("TestTag");

        var response = restTemplate.exchange(
                "/api/issues/1/tags/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                EntityModel.class
        );
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private Issue createIssue() {
        Issue issue = new Issue();
        issue.setName("Test issue");
        issue.setId(1L);
        return issue;
    }
}
