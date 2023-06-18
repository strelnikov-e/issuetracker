package com.strelnikov.issuetracker.controller;


import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;
    private final UserModelAssembler assembler;


    public UserRestController(UserService userService, UserModelAssembler assembler) {
        this.userService = userService;
        this.assembler = assembler;
    }

    /*
    Returns list of users assigned to the projects, where current user is MANAGER

     */
    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> all(@RequestParam Map<String, Object> params) {
        Set<EntityModel<User>> users = new HashSet<>();
        if (params == null || params.isEmpty()) {
            users = userService.findAll().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toSet());
            ;
        }
//        else {
//            for (var param: params.entrySet()) {
//                switch (param.getKey()) {
//                    case "name" -> issues = issueService.findByName(param.getValue().toString())
//                            .stream()
//                            .map(assembler::toModel)
//                            .collect(Collectors.toList());
//                    case "projectId" -> issues = issueService.findByProjectId(Long.parseLong(param.getValue().toString()))
//                            .stream()
//                            .map(assembler::toModel)
//                            .collect(Collectors.toList());
//                    case "status" -> issues = issueService.findByStatus(IssueStatus.valueOf(param.getValue().toString()))
//                            .stream()
//                            .map(assembler::toModel)
//                            .collect(Collectors.toList());
//                }
//            }

        return CollectionModel
                .of(users, linkTo(methodOn(UserRestController.class)
                        .all(params)).withSelfRel());
    }

//        List<EntityModel<User>> users = userService.findAll(username).stream()
//                .map(assembler::toModel)
//                .collect(Collectors.toList());
//
//        return CollectionModel.of(
//                users,
//                linkTo(methodOn(UserRestController.class)
//                        .all(username))
//                        .withSelfRel());
//    }

    /*
    Register new user.
    Open endpoint.
    Required fields: username, password, email, firstName, lastName
     */
    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody User requestUser) {
        EntityModel<User> entityModel = assembler.toModel(userService.save(requestUser));
        return ResponseEntity
                .created(entityModel
                        .getRequiredLink(IanaLinkRelations.SELF)
                        .toUri())
                .body(entityModel);
    }

    /*
    Update user details.
    User details can be updated only by the user himself.
     */
    @PutMapping("/users/{id}")
//    @PreAuthorize("@RoleService.hasAnyRoleByIssueId(#issueId, @IssueRole.ASSIGNEE)")
    public  ResponseEntity<?> update(@PathVariable Long id, @RequestBody User request) {
        EntityModel<User> entityModel = assembler.toModel(userService.update(id,request));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
                .toUri())
                .body(entityModel);
    }

    // to implement cascading delete of all user orphaned projects
    @DeleteMapping("/users/{id}")
    @PreAuthorize("@RoleService.hasRootRole()")
    public ResponseEntity<?> delete(@RequestParam(value="id") Long id) {
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
