package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.controller.UserRestController;
import com.strelnikov.issuetracker.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserShortModelAssembler extends RepresentationModelAssemblerSupport<User, UserShortModel> {

    public UserShortModelAssembler() {
        super(UserRestController.class, UserShortModel.class);
    }

    @Override
    public UserShortModel toModel(User entity) {
        UserShortModel model = new UserShortModel();
//        model.add(linkTo(
//                methodOn(UserRestController.class)
//                        .getById(entity.getId()))
//                .withSelfRel());
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
