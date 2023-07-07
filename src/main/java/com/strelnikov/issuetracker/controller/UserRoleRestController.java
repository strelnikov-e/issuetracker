package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.controller.hateoas.ProjectRoleModel;
import com.strelnikov.issuetracker.controller.hateoas.ProjectRoleModelAssembler;
import com.strelnikov.issuetracker.entity.ProjectRole;
import com.strelnikov.issuetracker.service.ProjectRoleService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/roles")
public class UserRoleRestController {

    private final ProjectRoleService projectRoleService;
    private final ProjectRoleModelAssembler assembler;
    private final PagedResourcesAssembler<ProjectRole> pagedResourcesAssembler;

    public UserRoleRestController(ProjectRoleService projectRoleService, ProjectRoleModelAssembler assembler, PagedResourcesAssembler<ProjectRole> pagedResourcesAssembler) {
        this.projectRoleService = projectRoleService;
        this.assembler = assembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/project")
    public CollectionModel<ProjectRoleModel> all(@RequestParam Map<String, String> params, Pageable pageable) {
        List<ProjectRoleModel> projectRoleModels;
        Long projectId = Long.parseLong(params.getOrDefault("project", "0"));
        projectRoleModels = projectRoleService.getAllByProjectId(projectId)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

//        return projectRoleModels;
        return CollectionModel.of(projectRoleModels)
                .add( linkTo(methodOn(UserRoleRestController.class).all(params, pageable)).withSelfRel());
    }

    @GetMapping("/{id}")
    public ProjectRoleModel getById(@PathVariable Long id) {
       ProjectRole projectRole =  projectRoleService.getById(id);
       return assembler.toModel(projectRole);
    }

     /*
    Create new role.
    Required fields: user.id, project.id, Role
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ProjectRoleModel create(@RequestBody ProjectRoleModel requestUser) {
        ProjectRoleModel entityModel = assembler.toModel(projectRoleService.save(requestUser));
        return entityModel;
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void patch(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new RequestRejectedException("Bad request. This may happen if request body is empty");
        }
        ProjectRoleModel entityModel = assembler.toModel(projectRoleService.patch(id, fields));
    }

}
