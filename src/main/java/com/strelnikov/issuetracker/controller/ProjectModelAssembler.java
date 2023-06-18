package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.entity.Project;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectModelAssembler implements RepresentationModelAssembler<Project, EntityModel<Project>> {
    @Override
    public EntityModel<Project> toModel(Project project) {
        return EntityModel.of(project,
                WebMvcLinkBuilder.linkTo(methodOn(ProjectRestController.class).getById(project.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(ProjectRestController.class).all(new HashMap<>())).withRel("projects"));

    }
}
