package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.controller.TagRestController;
import com.strelnikov.issuetracker.entity.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler extends RepresentationModelAssemblerSupport<Tag, TagModel> {

    public TagModelAssembler() {
        super(TagRestController.class, TagModel.class);
    }

    @Override
    public TagModel toModel(Tag entity) {
        TagModel model = new TagModel();
        model.add(linkTo(
                methodOn(TagRestController.class)
                        .getById(entity.getId()))
                .withSelfRel());
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
