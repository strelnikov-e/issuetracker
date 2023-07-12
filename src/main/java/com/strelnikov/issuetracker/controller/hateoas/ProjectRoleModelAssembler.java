package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.config.RoleService;
import com.strelnikov.issuetracker.controller.UserRoleRestController;
import com.strelnikov.issuetracker.entity.ProjectRole;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectRoleModelAssembler extends RepresentationModelAssemblerSupport<ProjectRole, ProjectRoleModel> {

    private final UserShortModelAssembler userModelAssembler;
    private final ProjectShortModelAssembler projectModelAssembler;
    private final RoleService roleService;

    public ProjectRoleModelAssembler(UserShortModelAssembler userModelAssembler, ProjectShortModelAssembler projectModelAssembler, RoleService roleService) {
        super(UserRoleRestController.class, ProjectRoleModel.class);
        this.userModelAssembler = userModelAssembler;
        this.projectModelAssembler = projectModelAssembler;
        this.roleService = roleService;
    }

    @Override
    public ProjectRoleModel toModel(ProjectRole entity) {
        ProjectRoleModel model = new ProjectRoleModel();
        ProjectRole role = new ProjectRole();
        model.add(linkTo(
                methodOn(UserRoleRestController.class)
                        .getById(entity.getId()))
                .withSelfRel());
        if (roleService.hasAnyRoleByProjectId(entity.getProject().getId(), ProjectRoleType.ADMIN)) {
            role.setRole(ProjectRoleType.ADMIN);
            model.add(linkTo(methodOn(UserRoleRestController.class)
                    .changeRole( entity.getId(), role)).withRel("setAdmin"));
            role.setRole(ProjectRoleType.MANAGER);
            model.add(linkTo(methodOn(UserRoleRestController.class)
                    .changeRole( entity.getId(), role)).withRel("setManager"));
            role.setRole(ProjectRoleType.VIEWER);
            model.add(linkTo(methodOn(UserRoleRestController.class)
                    .changeRole( entity.getId(), role)).withRel("setViewer"));
            model.add(linkTo(methodOn(UserRoleRestController.class)
                    .delete( entity.getId())).withRel("delete"));

        }
        if (roleService.hasAnyRoleByProjectId(entity.getProject().getId(), ProjectRoleType.MANAGER)
        && !entity.getRole().equals(ProjectRoleType.ADMIN)) {
            role.setRole(ProjectRoleType.MANAGER);
            model.add(linkTo(methodOn(UserRoleRestController.class)
                    .changeRole( entity.getId(), role)).withRel("setManager"));
            role.setRole(ProjectRoleType.VIEWER);
            model.add(linkTo(methodOn(UserRoleRestController.class)
                    .changeRole( entity.getId(), role)).withRel("setViewer"));
            model.add(linkTo(methodOn(UserRoleRestController.class)
                    .delete( entity.getId())).withRel("delete"));
        }
        model.setId(entity.getId());
        model.setRole(entity.getRole());
        model.setUser(userModelAssembler.toModel(entity.getUser()));
        model.setProject(projectModelAssembler.toModel(entity.getProject()));
        return model;
    }
}
