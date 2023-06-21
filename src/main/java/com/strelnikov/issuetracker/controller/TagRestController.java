package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.Tag;
import com.strelnikov.issuetracker.exception.IssueNotFoundException;
import com.strelnikov.issuetracker.service.IssueService;
import com.strelnikov.issuetracker.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api")
public class TagRestController {

    private final TagService tagService;
    private final IssueService issueService;
    private final TagModelAssembler assembler;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public TagRestController(TagService tagService, IssueService issueService, TagModelAssembler assembler) {
        this.tagService = tagService;
        this.issueService = issueService;
        this.assembler = assembler;
    }

    @GetMapping("/tags")
    public CollectionModel<EntityModel<Tag>> all(@RequestParam(value = "name", required = false) String name) {
        List<EntityModel<Tag>> tags = tagService.findByName(name).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(tags, linkTo(methodOn(TagRestController.class).all(name)).withSelfRel());
    }

    @GetMapping("/tags/{tagId}")
    public EntityModel<Tag> getById(@PathVariable Long tagId) {
        Tag tag = tagService.findById(tagId);
        return assembler.toModel(tag);
    }

    @GetMapping("/issues/{issueId}/tags")
    @PreAuthorize("@RoleService.hasAnyRoleByIssueId(#issueId, @IssueRole.VIEWER)")
    public CollectionModel<EntityModel<Tag>> getTagsByIssueId(@PathVariable Long issueId) {
        Issue issue = issueService.findById(issueId);
        if (issue == null) {
            throw new IssueNotFoundException(issueId);
        }
        List<EntityModel<Tag>> tags = issue.getTags().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(tags, linkTo(methodOn(TagRestController.class).getTagsByIssueId(issueId)).withSelfRel());
    }

    @PostMapping("/issues/{issueId}/tags")
    @PreAuthorize("@RoleService.hasAnyRoleByIssueId(#issueId, @IssueRole.ASSIGNEE)")
    public ResponseEntity<?> addTag(@PathVariable Long issueId, @RequestBody Tag tagRequest) {
        EntityModel<Tag> entityModel = assembler.toModel(tagService.addTag(issueId, tagRequest));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/issues/{issueId}/tags/{tagId}")
    @PreAuthorize("@RoleService.hasAnyRoleByIssueId(#issueId, @IssueRole.ASSIGNEE)")
    public ResponseEntity<?> deleteTagFromIssue(@PathVariable Long tagId, @PathVariable Long issueId) {
        Issue issue = issueService.findById(issueId);
        Tag tag = tagService.findById(tagId);
        issue.getTags().remove(tag);
        issueService.save(issue);
        return ResponseEntity.noContent().build();
    }


}
