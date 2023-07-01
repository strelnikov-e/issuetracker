package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.controller.ProjectRestController;
import com.strelnikov.issuetracker.entity.Project;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProjectModelAssembler extends RepresentationModelAssemblerSupport<Project, ProjectModel> {

    public ProjectModelAssembler() {
        super(ProjectRestController.class, ProjectModel.class);
    }

    @Override
    public ProjectModel toModel(Project entity) {
        ProjectModel model = new ProjectModel();
        model.add(linkTo(
                methodOn(ProjectRestController.class)
                        .getById(entity.getId()))
                .withSelfRel());
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
