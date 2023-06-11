package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.entity.Project;
import com.strelnikov.issuetracker.exception.ProjectNotFoundException;
import com.strelnikov.issuetracker.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
//@CrossOrigin("http://localhost:3000")
public class ProjectRestController {

    private ProjectService projectService;
    private ProjectModelAssembler assembler;

    public ProjectRestController(ProjectService projectService, ProjectModelAssembler assembler) {
        this.projectService = projectService;
        this.assembler = assembler;
    }

//    // Get all projects of current user
//    @GetMapping("/projects")
//    public List<Project> all(@RequestParam(value = "name", defaultValue = "", required = false) String name) {
//        List<Project> projects = projectService.findByName(name);
//        return projects;
//    }


//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    // Get all projects of current user
    @GetMapping("/projects")
    public CollectionModel<EntityModel<Project>> all(@RequestParam(value = "name", defaultValue = "", required = false) String name) {
        List<EntityModel<Project>> projects = projectService.findByName(name).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(projects, linkTo(methodOn(ProjectRestController.class).all(name)).withSelfRel());
    }

    // Get project by ID
    @GetMapping("/projects/{projectId}")
    public EntityModel<Project> getById(@PathVariable Long projectId) {
        Project project = projectService.findById(projectId);
        if (project == null) {
            throw new ProjectNotFoundException(projectId);
        }
        return assembler.toModel(project);
    }

    // Create new project and set project admin to current user
    @PostMapping("/projects")
    public ResponseEntity<?> createProject(@Valid @RequestBody Project project) {
        EntityModel<Project> entityModel = assembler.toModel(projectService.create(project));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    // Update project
    @PutMapping("/projects/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody Project project) {
        project.setId(projectId);
        EntityModel<Project> entityModel = assembler.toModel(projectService.update(project));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    // delete project and all the issues of the project
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        projectService.deleteById(projectId);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/project/{projectId}/status")
//    public Set<IssueStatus> status(@PathVariable Long projectId) {
//        Set<IssueStatus> statuses = projectService.findById(projectId).getIssueStatuses();
//        return statuses;
//    }

}
