package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.entity.Project;
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

import java.time.LocalDate;
import java.util.ArrayList;

public class ProjectControllerTest extends AbstractControllerTest {

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
    void shouldReturn401UnathorizedUserTryingToGetProjects() {
        final var projectGetResponse =
                restTemplate.getForEntity("/api/projects", ResponseEntity.class);
        Assertions.assertEquals(projectGetResponse.getStatusCode(),HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldGetProjectsListSuccesfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/projects",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CollectionModel.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(2,response.getBody().getContent().size());
    }

    @Test
    void shouldGetProjectSuccesfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/projects/1",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                EntityModel.class
        );
        System.out.println(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().toString().contains("Issuetracker"));
    }

    @Test
    void shouldReturn401IfUnathorizedUserTryingToCreateProject() {
        Project project = new Project(
                "Test project",
                "TP",
                new ArrayList<>(),
                "Test project description",
                LocalDate.now(),
                "www.test.com");
        final var projectCreatedResponse =
                restTemplate.postForEntity(
                        "/api/projects",
                        project,
                        ResponseEntity.class
                );
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, projectCreatedResponse.getStatusCode());
    }

    @Test
    void shouldCreateProjectAndIssueSuccesfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        Project project = createProject();
        HttpEntity<Project> body = new HttpEntity<>(project);
        var response = restTemplate.exchange(
                "/api/projects",
                HttpMethod.POST,
                new HttpEntity<>(project, headers),
                String.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("\"id\":3,\"name\":\"Test project\""));
    }

    @Test
    void shouldReturn403ForbiddenTryingUpdateByUnathorizedUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForViewer());

        Project project = createProject();
        HttpEntity<Project> body = new HttpEntity<>(project);
        var response = restTemplate.exchange(
                "/api/projects/1",
                HttpMethod.PUT,
                new HttpEntity<>(body, headers),
                EntityModel.class
        );
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void shouldUpdateProjectSucessfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        Project project = createProject();
        HttpEntity<Project> body = new HttpEntity<>(project);
        var response = restTemplate.exchange(
                "/api/projects/1",
                HttpMethod.PUT,
                new HttpEntity<>(project, headers),
                String.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("Test project"));
    }

    @Test
    void shouldReturn403ForbiddenTryingDeleteByUnathorizedUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForViewer());

        var response = restTemplate.exchange(
                "/api/projects/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                EntityModel.class
        );
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void shouldDeleteProjectSuccesfully() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", tokenPrefix + getTokenForAdmin());

        var response = restTemplate.exchange(
                "/api/projects/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                EntityModel.class
        );
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    private Project createProject() {
        Project project = new Project();
        project.setName("Test project");
        project.setKey("TP");
        return project;
    }
}
