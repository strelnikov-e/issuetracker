package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.controller.hateoas.ProjectModel;
import com.strelnikov.issuetracker.controller.hateoas.ProjectModelAssembler;
import com.strelnikov.issuetracker.entity.Project;
import com.strelnikov.issuetracker.exception.exception.ProjectNotFoundException;
import com.strelnikov.issuetracker.service.ProjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectRestController.class);

    private final ProjectService projectService;
    private final ProjectModelAssembler assembler;
    private final PagedResourcesAssembler<Project> pagedResourcesAssembler;

    public ProjectRestController(ProjectService projectService, ProjectModelAssembler assembler, PagedResourcesAssembler<Project> pagedResourcesAssembler) {
        this.projectService = projectService;
        this.assembler = assembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    /*
    Return list of the projects where current user any role.
    */
    @GetMapping
    public CollectionModel<ProjectModel> all(@RequestParam Map<String, Object> params, Pageable pageable) {

        Page<Project> projects;
        if (params.containsKey("name")) {
            projects = projectService
                    .findByName(params.get("name").toString() , pageable);
        } else {
            projects = projectService
                    .findAll(pageable);
        }
        return pagedResourcesAssembler.toModel(projects, assembler);
    }

    /*
    Return project by ID if authenticated user has any role in requested project.
     */
    @GetMapping("/{projectId}")
    @PreAuthorize("@RoleService.hasAnyRoleByProjectId(#projectId, @ProjectRole.VIEWER)")
    public ProjectModel getById(@PathVariable Long projectId) {
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
    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectModel project) {
        LOG.debug("POST request to create project: '{}'", project);
        ProjectModel entityModel = assembler.toModel(projectService.create(project));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /*
    Update project by ID passed as path variable.
    User should have role of MANAGER or above for the project.
     */
    @PutMapping("/{projectId}")
    @PreAuthorize("@RoleService.hasAnyRoleByProjectId(#projectId, @ProjectRole.MANAGER)")
    public ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody ProjectModel project) {
        LOG.debug("PUT Request to update project with ID: '{}'. New values: '{}'", projectId, project.toString());
        project.setId(projectId);
        ProjectModel entityModel = assembler.toModel(projectService.update(project));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /*
    Delete project by ID passed as path variable.
    User should have role of ADMIN for the project.
     */
    @DeleteMapping("/{projectId}")
    @PreAuthorize("@RoleService.hasAnyRoleByProjectId(#projectId, @ProjectRole.ADMIN)")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        LOG.debug("Delete request for project with ID: '{}'", projectId);
        projectService.deleteById(projectId);
        return ResponseEntity.noContent().build();
    }

}



