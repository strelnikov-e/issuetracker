package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.config.RoleService;
import com.strelnikov.issuetracker.controller.IssueRestController;
import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.IssueRoleType;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class IssueModelAssembler extends RepresentationModelAssemblerSupport<Issue, IssueModel> {

    private final UserService userService;
    private final UserModelAssembler assembler;
    private final RoleService roleService;

    public IssueModelAssembler(UserService userService, UserModelAssembler assembler, RoleService roleService) {
        super(IssueRestController.class, IssueModel.class);
        this.userService = userService;
        this.assembler = assembler;
        this.roleService = roleService;
    }

//    public IssueModelAssembler() {
//        super(IssueRestController.class, IssueModel.class);
//    }

    @Override
    public IssueModel toModel(Issue entity) {
        IssueModel model = new IssueModel();
        model.add(linkTo(
                methodOn(IssueRestController.class)
                        .getById(entity.getId()))
                .withSelfRel());
        if (roleService.hasAnyRoleByProjectId(entity.getProject().getId(), ProjectRoleType.MANAGER)) {
            model.add(linkTo(methodOn(IssueRestController.class)
                    .deleteIssue( entity.getId())).withRel("delete"));
        }
        if (roleService.hasAnyRoleByIssueId(entity.getId(), IssueRoleType.ASSIGNEE)) {
            model.add(linkTo(methodOn(IssueRestController.class)
                    .updateIssue(entity.getId(), new IssueModel())).withRel("update"));
        }
        BeanUtils.copyProperties(entity, model);
        User assignee = userService.findByIssueRole(entity.getId(), IssueRoleType.ASSIGNEE);
        User reporter = userService.findByIssueRole(entity.getId(), IssueRoleType.REPORTER);
        model.setAssignee(assembler.toModel(assignee));
        model.setReporter(assembler.toModel(reporter));
        return model;
    }
}
