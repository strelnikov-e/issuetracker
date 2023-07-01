package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.controller.UserRestController;
import com.strelnikov.issuetracker.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

    public UserModelAssembler() {
        super(UserRestController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(User entity) {
        UserModel model = new UserModel();
//        model.add(linkTo(
//                methodOn(UserRestController.class)
//                        .getById(entity.getId()))
//                .withSelfRel());
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
