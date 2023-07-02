package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.controller.IssueRestController;
import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.IssueRoleType;
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

    public IssueModelAssembler(UserService userService, UserModelAssembler assembler) {
        super(IssueRestController.class, IssueModel.class);
        this.userService = userService;
        this.assembler = assembler;
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
        BeanUtils.copyProperties(entity, model);
        User assignee = userService.findByIssueRole(entity.getId(), IssueRoleType.ASSIGNEE);
        User reporter = userService.findByIssueRole(entity.getId(), IssueRoleType.REPORTER);
        model.setAssignee(assembler.toModel(assignee));
        model.setReporter(assembler.toModel(reporter));
        return model;
    }
}
