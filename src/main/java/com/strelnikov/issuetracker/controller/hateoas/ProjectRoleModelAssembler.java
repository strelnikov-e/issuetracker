package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.controller.UserRoleRestController;
import com.strelnikov.issuetracker.entity.ProjectRole;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectRoleModelAssembler extends RepresentationModelAssemblerSupport<ProjectRole, ProjectRoleModel> {

    private final UserModelAssembler userModelAssembler;
    private final ProjectModelAssembler projectModelAssembler;

    public ProjectRoleModelAssembler(UserModelAssembler userModelAssembler, ProjectModelAssembler projectModelAssembler) {
        super(UserRoleRestController.class, ProjectRoleModel.class);
        this.userModelAssembler = userModelAssembler;
        this.projectModelAssembler = projectModelAssembler;
    }

    @Override
    public ProjectRoleModel toModel(ProjectRole entity) {
        ProjectRoleModel model = new ProjectRoleModel();
        model.add(linkTo(
                methodOn(UserRoleRestController.class)
                        .getById(entity.getId()))
                .withSelfRel());
        model.setId(entity.getId());
        model.setRole(entity.getRole());
        model.setUser(userModelAssembler.toModel(entity.getUser()));
        model.setProject(projectModelAssembler.toModel(entity.getProject()));
        return model;
    }
}
