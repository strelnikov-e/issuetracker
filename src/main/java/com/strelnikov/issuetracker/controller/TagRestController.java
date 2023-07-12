package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.controller.hateoas.TagModel;
import com.strelnikov.issuetracker.controller.hateoas.TagModelAssembler;
import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.Tag;
import com.strelnikov.issuetracker.exception.exception.IssueNotFoundException;
import com.strelnikov.issuetracker.service.IssueService;
import com.strelnikov.issuetracker.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/api")
public class TagRestController {

    private final TagService tagService;
    private final IssueService issueService;
    private final TagModelAssembler assembler;
    private final PagedResourcesAssembler<Tag> pagedResourcesAssembler;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public TagRestController(TagService tagService, IssueService issueService, TagModelAssembler assembler, PagedResourcesAssembler<Tag> pagedResourcesAssembler) {
        this.tagService = tagService;
        this.issueService = issueService;
        this.assembler = assembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/tags")
    public CollectionModel<TagModel> all(@RequestParam(value = "name", required = false) String name, Pageable pageable) {
        Page<Tag> tags = tagService.findByName(name, pageable);
        return pagedResourcesAssembler.toModel(tags, assembler);
    }

    @GetMapping("/tags/{tagId}")
    public TagModel getById(@PathVariable Long tagId) {
        Tag tag = tagService.findById(tagId);
        return assembler.toModel(tag);
    }

    // Refactor to return Page<Tag> instead of Set<Tag>
    @GetMapping("/issues/{issueId}/tags")
    @PreAuthorize("@RoleService.hasAnyRoleByIssueId(#issueId, @IssueRole.VIEWER)")
    public CollectionModel<TagModel> getTagsByIssueId(@PathVariable Long issueId) {
        Issue issue = issueService.findById(issueId);
        if (issue == null) {
            throw new IssueNotFoundException(issueId);
        }
        Set<Tag> tags = issue.getTags();
        return pagedResourcesAssembler.toModel((Page<Tag>) tags, assembler);
    }

    @PostMapping("/issues/{issueId}/tags")
    @PreAuthorize("@RoleService.hasAnyRoleByIssueId(#issueId, @IssueRole.ASSIGNEE)")
    public ResponseEntity<?> addTag(@PathVariable Long issueId, @RequestBody Tag tagRequest) {
        TagModel entityModel = assembler.toModel(tagService.addTag(issueId, tagRequest));

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
