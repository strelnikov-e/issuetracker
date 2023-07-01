package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.controller.hateoas.IssueModel;
import com.strelnikov.issuetracker.controller.hateoas.IssueModelAssembler;
import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.IssueStatus;
import com.strelnikov.issuetracker.service.IssueService;
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

import java.util.Map;

@RestController
@RequestMapping("/api")
public class IssueRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectRestController.class);

    private final IssueService issueService;
    private final IssueModelAssembler assembler;
    private final IssueStatusModelAssembler issueStatusModelAssembler;
    private final PagedResourcesAssembler<Issue> pagedResourcesAssembler;

    public IssueRestController(IssueService issueService, IssueModelAssembler assembler, IssueStatusModelAssembler issueStatusModelAssembler, PagedResourcesAssembler<Issue> pagedResourcesAssembler) {
        this.issueService = issueService;
        this.assembler = assembler;
        this.issueStatusModelAssembler = issueStatusModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    /*
    Return list of the issues where current user any role.
     */
    @GetMapping("/issues")
    @PreAuthorize("isAuthenticated")
    public CollectionModel<IssueModel> all(@RequestParam Map<String, Object> params, Pageable pageable) {

        Page<Issue> issues;
        if (params.containsKey("name")) {
            issues = issueService
                    .findByName(params.get("name").toString(), pageable);
        } else if (params.containsKey("project")) {
            issues = issueService
                    .findByProjectId(Long.parseLong(params.get("project").toString()), pageable);
        } else if (params.containsKey("status")) {
            issues = issueService
                    .findByStatus(IssueStatus.valueOf(params.get("status").toString()), pageable);
        } else {
            issues = issueService
                    .findAll(pageable);
        }
        return pagedResourcesAssembler.toModel(issues, assembler);
    }

    /*
    Return an issue if it exists and if User authorized to view the issue.
     */
    @GetMapping("/issues/{issueId}")
    @PreAuthorize("@RoleService.hasAnyRoleByIssueId(#issueId, @IssueRole.VIEWER)")
    public IssueModel getById(@PathVariable Long issueId) {
        Issue issue = issueService.findById(issueId);
        return assembler.toModel(issue);
    }

    /*
    Create new issue for the project.
    IssueRole.REPORTER will be assigned to the user.
    Return 403 if user is not have assigned any project role.
    Required fields: issue.name, project.id
     */
    @PostMapping("/issues")
    @PreAuthorize("@RoleService.hasAnyRoleByProjectId(#requestIssue.project.id, @ProjectRole.VIEWER)")
    public ResponseEntity<?> createIssue(@RequestBody Issue requestIssue) {
        LOG.debug("POST Request to create new issue: '{}'", requestIssue.toString());
        IssueModel entityModel = assembler.toModel(issueService.create(requestIssue));
        return ResponseEntity
                .created(entityModel
                        .getRequiredLink(IanaLinkRelations.SELF)
                        .toUri())
                .body(entityModel);
    }

    /*
    Update an issue for the project.
    Method wil set fields to NULL (aside from id, name, project and key) if the field was not set in the request body.
    Return 403 if user is not have an ASSIGNEE ROLE or above.
     */
    @PutMapping("/issues/{issueId}")
    @PreAuthorize("@RoleService.hasAnyRoleByIssueId(#issueId, @IssueRole.ASSIGNEE)")
    public ResponseEntity<?> updateIssue(@PathVariable Long issueId, @RequestBody Issue requestIssue) {
        LOG.debug("PUT Request to update issue with ID: '{}'. New values: '{}'", issueId, requestIssue.toString());
        IssueModel entityModel = assembler.toModel(issueService.update(issueId,requestIssue));
         return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
                 .toUri())
                 .body(entityModel);
    }

    /*
    Update an issue for the project.
    Method wil set fields to NULL (aside from id, name, project and key) if the field was not set in the request body.
    Return 403 if user is not have an ASSIGNEE ROLE or above.
     */
    @PatchMapping("/issues/{issueId}")
    @PreAuthorize("@RoleService.hasAnyRoleByIssueId(#issueId, @IssueRole.ASSIGNEE)")
    public ResponseEntity<?> patchIssue(@PathVariable Long issueId, @RequestBody Map<String, Object> fields) {
        if (issueId.compareTo(0L) < 1 || fields == null || fields.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        IssueModel entityModel = assembler.toModel(issueService.patch(issueId, fields));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
                .toUri())
                .body(entityModel);
    }

    /*
   Delete issue by ID passed as path variable.
   User should be a project MANAGER.
    */
    @DeleteMapping("/issues/{issueId}")
    public ResponseEntity<?> deleteIssue(@PathVariable Long issueId) {
        LOG.debug("Delete request for project with ID: '{}'", issueId);
        issueService.deleteById(issueId);
        return ResponseEntity.noContent().build();
    }
}
