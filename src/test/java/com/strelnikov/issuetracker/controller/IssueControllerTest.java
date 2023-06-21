package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.entity.*;
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

public class IssueControllerTest extends AbstractControllerTest {

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
    void shouldReturn401UnauthorizedUserTryingToGetIssues() {
        final var response =
                restTemplate.getForEntity("/api/issues", ResponseEntity.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturn403ForbiddenUserTryingToGetIssues() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForViewer());

        var response = restTemplate.exchange(
                "/api/issues/5",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CollectionModel.class
        );
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void shouldGetIssuesListForCurrentUserSuccessfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/issues",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CollectionModel.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(6,response.getBody().getContent().size());
    }

    @Test
    void shouldGetIssuesListForProjectAndByNameSuccessfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/issues?name=user",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CollectionModel.class
        );
        System.out.println(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(2,response.getBody().getContent().size());
    }

    @Test
    void shouldGetIssueSuccessfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/issues/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                EntityModel.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().getContent().toString().contains("Create issues database"));
    }

    @Test
    void shouldReturn401IfUnauthorizedUserTryingToCreateIssue() {
        HttpHeaders headers = new HttpHeaders();
        var issue = createIssue();

        var response = restTemplate.exchange(
                "/api/issues",
                HttpMethod.POST,
                new HttpEntity<>(issue, headers),
                CollectionModel.class
        );
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void shouldCreateIssueSuccessfully() {
        Issue issue = createIssue();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/issues",
                HttpMethod.POST,
                new HttpEntity<>(issue, headers),
                EntityModel.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertTrue(response.getBody().getContent().toString().contains("Test issue"));
    }

    @Test
    void shouldReturn403ForbiddenTryingToCreateIssueByUnauthorizedUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForViewer());

        Issue issue = createIssue();

        var response = restTemplate.exchange(
                "/api/issues",
                HttpMethod.POST,
                new HttpEntity<>(issue, headers),
                EntityModel.class
        );
        System.out.println(response);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void shouldReturn403ForbiddenTryingUpdateIssueByUnauthorizedUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForViewer());
        Issue issue = createIssue();

        var response = restTemplate.exchange(
                "/api/issues/5",
                HttpMethod.PUT,
                new HttpEntity<>(issue, headers),
                EntityModel.class
        );
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void shouldUpdateIssueSuccessfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());
        Issue issue = createIssue();

        var response = restTemplate.exchange(
                "/api/issues/1",
                HttpMethod.PUT,
                new HttpEntity<>(issue, headers),
                String.class
        );
        System.out.println(response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        Assertions.assertTrue(response.getBody().getContent().toString().contains("id=1, name=Test issue"));
    }

    @Test
    void shouldReturn403ForbiddenTryingDeleteIssueByUnauthorizedUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForViewer());

        var response = restTemplate.exchange(
                "/api/issues/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void shouldDeleteIssueSuccessfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/issues/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                EntityModel.class
        );
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private Issue createIssue() {
        Issue issue = new Issue();
        issue.setName("Test issue");
        issue.setPriority(IssuePriority.MEDIUM);
        issue.setType(IssueType.TASK);
        issue.setStatus(IssueStatus.TODO);
        Project project = new Project();
        project.setId(2L);
        issue.setProject(project);
        return issue;
    }
}
