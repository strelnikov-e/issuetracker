package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.entity.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(
                user,
                linkTo(methodOn(UserRestController.class).all(new HashMap<>()))
                        .withSelfRel(),
                linkTo(methodOn(UserRestController.class).all(new HashMap<>()))
                        .withRel("users")
        );
    }
}
