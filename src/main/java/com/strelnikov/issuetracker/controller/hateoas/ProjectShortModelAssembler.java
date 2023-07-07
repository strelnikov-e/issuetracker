package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.controller.ProjectRestController;
import com.strelnikov.issuetracker.entity.Project;
import com.strelnikov.issuetracker.service.ProjectRoleService;
import com.strelnikov.issuetracker.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectShortModelAssembler extends RepresentationModelAssemblerSupport<Project, ProjectShortModel> {

    private final UserService userService;
    private final ProjectRoleService projectRoleService;
    private final UserModelAssembler assembler;

    public ProjectShortModelAssembler(UserService userService, ProjectRoleService projectRoleService, UserModelAssembler assembler) {
        super(ProjectRestController.class, ProjectShortModel.class);
        this.userService = userService;
        this.projectRoleService = projectRoleService;
        this.assembler = assembler;
    }

    @Override
    public ProjectShortModel toModel(Project entity) {
        ProjectShortModel model = new ProjectShortModel();
        model.add(linkTo(
                methodOn(ProjectRestController.class)
                        .getById(entity.getId()))
                .withSelfRel());
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
