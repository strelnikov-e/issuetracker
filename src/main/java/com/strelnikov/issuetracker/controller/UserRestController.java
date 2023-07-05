package com.strelnikov.issuetracker.controller;


import com.strelnikov.issuetracker.controller.hateoas.UserModel;
import com.strelnikov.issuetracker.controller.hateoas.UserModelAssembler;
import com.strelnikov.issuetracker.entity.IssueRoleType;
import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final UserModelAssembler assembler;
    private final PagedResourcesAssembler<User> pagedResourcesAssembler;


    public UserRestController(UserService userService, UserModelAssembler assembler, PagedResourcesAssembler<User> pagedResourcesAssembler) {
        this.userService = userService;
        this.assembler = assembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    /*
    Returns list of users assigned to the projects, where current user is MANAGER
     */
    @GetMapping
    public CollectionModel<UserModel> all(@RequestParam Map<String, Object> params, Pageable pageable) {
        List<UserModel> users;
        Long projectId = Long.parseLong(params.getOrDefault("project", 0L).toString());
        if (params.containsKey("issueRole")) {
            users = userService.findByProjectIdAndIssueRole(projectId, IssueRoleType.valueOf(params.get("issueRole").toString()))
                    .stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
        } else {
            users = userService.findByProjectId(projectId)
                    .stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
        }

        return CollectionModel.of(users)
                .add( linkTo(methodOn(UserRestController.class).all(params, pageable)).withSelfRel());
    }

    @GetMapping("/details")
    public UserModel userDetails() {
        User user = userService.getCurrentUser();
        return assembler.toModel(user);
    }

    /*
    Register new user.
    Open endpoint.
    Required fields: username, password, email, firstName, lastName
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody User requestUser) {
        UserModel entityModel = assembler.toModel(userService.save(requestUser));
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
    @PutMapping("/{id}")
    public  ResponseEntity<?> update(@PathVariable Long id, @RequestBody User request) {
        UserModel entityModel = assembler.toModel(userService.update(id,request));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
                .toUri())
                .body(entityModel);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void patch(@RequestBody Map<String, Object> fields) {
        if (fields == null || fields.isEmpty()) {
            throw new RequestRejectedException("Bad request. This may happen if request body is empty");
        }
        UserModel entityModel = assembler.toModel(userService.patch(fields));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete() {
        userService.delete();
        return ResponseEntity.noContent().build();
    }
}
