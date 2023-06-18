package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.entity.Project;
import com.strelnikov.issuetracker.exception.ProjectNotFoundException;
import com.strelnikov.issuetracker.service.ProjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class ProjectRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectRestController.class);

    private final ProjectService projectService;
    private final ProjectModelAssembler assembler;

    public ProjectRestController(ProjectService projectService, ProjectModelAssembler assembler) {
        this.projectService = projectService;
        this.assembler = assembler;
    }

    /*
    Return list of the projects where current user any role.
     */
    @GetMapping("/projects")
    public CollectionModel<EntityModel<?>> all(@RequestParam Map<String, Object> params) {
        List<EntityModel<?>> projects = new ArrayList<>();
        if (params == null || params.isEmpty()) {
            projects = projectService.findAll().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());;
        } else {
            for (var param: params.entrySet()) {
                switch (param.getKey()) {
                    case "name" -> projects = projectService.findByName(param.getValue().toString())
                            .stream()
                            .map(assembler::toModel)
                            .collect(Collectors.toList());
//                    case "manager" -> projects = projectService.findByManager(Long.parseLong(param.getValue().toString()))
//                            .stream()
//                            .map(assembler::toModel)
//                            .collect(Collectors.toList());
                }
            }
        }
        return CollectionModel
                .of(projects, linkTo(methodOn(ProjectRestController.class)
                .all(params)).withSelfRel());
    }

    /*
    Return project by ID if authenticated user has any role in requested project.
     */
    @GetMapping("/projects/{projectId}")
    @PreAuthorize("@RoleService.hasAnyRoleByProjectId(#projectId, @ProjectRole.VIEWER)")
    public EntityModel<Project> getById(@PathVariable Long projectId) {
        Project project = projectService.findById(projectId);
        if (project == null) {
            throw new ProjectNotFoundException(projectId);
        }
        return assembler.toModel(project);
    }

    /*
    Create a new project. Request should contain name of the project.
    User will get an ADMIN role for the project.
     */
    @PostMapping("/projects")
    public ResponseEntity<?> createProject(@Valid @RequestBody Project project) {
        LOG.debug("POST request to create project: '{}'", project);
        EntityModel<Project> entityModel = assembler.toModel(projectService.create(project));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /*
    Update project by ID passed as path variable.
    User should have role of MANAGER or above for the project.
     */
    @PutMapping("/projects/{projectId}")
    @PreAuthorize("@RoleService.hasAnyRoleByProjectId(#projectId, @ProjectRole.MANAGER)")
    public ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody Project project) {
        LOG.debug("PUT Request to update project with ID: '{}'. New values: '{}'", projectId, project.toString());
        project.setId(projectId);
        EntityModel<Project> entityModel = assembler.toModel(projectService.update(project));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /*
    Delete project by ID passed as path variable.
    User should have role of ADMIN for the project.
     */
    @DeleteMapping("/projects/{projectId}")
    @PreAuthorize("@RoleService.hasAnyRoleByProjectId(#projectId, @ProjectRole.ADMIN)")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        LOG.debug("Delete request for project with ID: '{}'", projectId);
        projectService.deleteById(projectId);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/project/{projectId}/status")
//    public Set<IssueStatus> status(@PathVariable Long projectId) {
//        Set<IssueStatus> statuses = projectService.findById(projectId).getIssueStatuses();
//        return statuses;
//    }

}
