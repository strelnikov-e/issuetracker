package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.entity.IssueStatus;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


// blank method. Need to revise
@Component
public class IssueStatusModelAssembler implements RepresentationModelAssembler<IssueStatus, EntityModel<IssueStatus>> {
    @Override
    public EntityModel<IssueStatus> toModel(IssueStatus status) {
        return EntityModel.of(status,
                linkTo(methodOn(StatusController.class).getStatus(status.name())).withSelfRel(),
                linkTo(methodOn(StatusController.class).all()).withRel("statuses"));

    }
}
