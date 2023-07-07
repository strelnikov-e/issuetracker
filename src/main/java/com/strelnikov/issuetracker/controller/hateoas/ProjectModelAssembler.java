package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.controller.ProjectRestController;
import com.strelnikov.issuetracker.entity.Project;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import com.strelnikov.issuetracker.service.ProjectRoleService;
import com.strelnikov.issuetracker.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectModelAssembler extends RepresentationModelAssemblerSupport<Project, ProjectModel> {

    private final UserService userService;
    private final ProjectRoleService projectRoleService;
    private final UserModelAssembler assembler;

    public ProjectModelAssembler(UserService userService, ProjectRoleService projectRoleService, UserModelAssembler assembler) {
        super(ProjectRestController.class, ProjectModel.class);
        this.userService = userService;
        this.projectRoleService = projectRoleService;
        this.assembler = assembler;
    }

    @Override
    public ProjectModel toModel(Project entity) {
        ProjectModel model = new ProjectModel();
        model.add(linkTo(
                methodOn(ProjectRestController.class)
                        .getById(entity.getId()))
                .withSelfRel());
        BeanUtils.copyProperties(entity, model);
        List<UserModel> managers = projectRoleService.findByProjectIdAndRole(entity.getId(), ProjectRoleType.MANAGER)
                .stream()
                .map(projectRole -> assembler.toModel(projectRole.getUser()))
                .collect(Collectors.toList());
        List<UserModel> admins = projectRoleService.findByProjectIdAndRole(entity.getId(), ProjectRoleType.ADMIN)
                .stream()
                .map(projectRole -> assembler.toModel(projectRole.getUser()))
                .collect(Collectors.toList());

        model.setManagers(managers);
        model.setAdmins(admins);
        return model;
    }
}
