package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.config.RoleService;
import com.strelnikov.issuetracker.controller.hateoas.IssueModel;
import com.strelnikov.issuetracker.controller.hateoas.IssueModelAssembler;
import com.strelnikov.issuetracker.controller.hateoas.IssueStatusModelAssembler;
import com.strelnikov.issuetracker.entity.*;
import com.strelnikov.issuetracker.service.IssueService;
import com.strelnikov.issuetracker.service.UserService;
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

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/issues")
public class IssueRestController {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectRestController.class);

    private final RoleService roleService;
    private final UserService userService;
    private final IssueService issueService;
    private final IssueModelAssembler assembler;
    private final IssueStatusModelAssembler issueStatusModelAssembler;
    private final PagedResourcesAssembler<Issue> pagedResourcesAssembler;

    public IssueRestController(RoleService roleService, UserService userService, IssueService issueService, IssueModelAssembler assembler, IssueStatusModelAssembler issueStatusModelAssembler, PagedResourcesAssembler<Issue> pagedResourcesAssembler) {
        this.roleService = roleService;
        this.userService = userService;
        this.issueService = issueService;
        this.assembler = assembler;
        this.issueStatusModelAssembler = issueStatusModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    /*
    Return list of the issues where current user any role.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated")
    public CollectionModel<IssueModel> all(@RequestParam Map<String, Object> params, Pageable pageable) {

        Page<Issue> issues;
        Long projectId = Long.parseLong(params.getOrDefault("project", 0L).toString());
        Boolean incomplete = params.containsKey("incomplete");

        if (!projectId.equals(0L) && !roleService.hasAnyRoleByProjectId(projectId, ProjectRoleType.VIEWER)) {
            // check if current user has right to see issues of the project
            // if query doesn't specify -> return issues for all project available for user by his role
            issues = Page.empty();
        } else if (params.containsKey("name")) {
            issues = issueService
                    .findByName(params.get("name").toString(), projectId, incomplete, pageable);
        } else if (params.containsKey("status")) {
            issues = issueService
                    .findByStatus(IssueStatus.valueOf(params.get("status").toString()), projectId, pageable);
        } else if (params.containsKey("priority")) {
            issues = issueService
                    .findByPriority(IssuePriority.valueOf(params.get("priority").toString()), projectId, incomplete, pageable);
        } else if (params.containsKey("type")) {
            issues = issueService
                    .findByType(IssueType.valueOf(params.get("type").toString()), projectId, incomplete, pageable);
        } else if (params.containsKey("assignee")) {
            issues = issueService
                    .findByUserRole(IssueRoleType.ASSIGNEE, Long.parseLong(params.get("assignee").toString()), projectId, incomplete, pageable);
        } else if (params.containsKey("reporter")) {
            issues = issueService
                    .findByUserRole(IssueRoleType.REPORTER, Long.parseLong(params.get("reporter").toString()), projectId, incomplete, pageable);
        } else if (params.containsKey("dueDate")) {
            issues = issueService
                    .findBeforeDueDate(LocalDate.parse(params.get("dueDate").toString()), projectId, incomplete, pageable);
        } else {
            issues = issueService
                    .findByProjectId(projectId, incomplete, pageable);
        }
        return pagedResourcesAssembler.toModel(issues, assembler);
    }

    @GetMapping("/assignee/{assigneeId}")
    public CollectionModel<IssueModel> getForAssignee(@PathVariable Long assigneeId, @RequestParam Map<String, Object> params, Pageable pageable) {

        Page<Issue> issues;
        Long projectId = Long.parseLong(params.getOrDefault("project", 0L).toString());
        Boolean incomplete = params.containsKey("incomplete");

        if (!projectId.equals(0L) && !roleService.hasAnyRoleByProjectId(projectId, ProjectRoleType.VIEWER)) {
            // check if current user has right to see issues of the project
            // if query doesn't specify -> return issues for all project available for user by his role
            issues = Page.empty();
        } else {
            issues = issueService
                    .findByUserRole(IssueRoleType.ASSIGNEE, assigneeId, projectId, incomplete, pageable);
        }
        return pagedResourcesAssembler.toModel(issues, assembler);
    }

    @GetMapping("/mywork")
    public CollectionModel<IssueModel> getMyWork(@RequestParam Map<String, Object> params, Pageable pageable) {
        Long userId = userService.getCurrentUser().getId();
        Page<Issue> issues;
        Long projectId = Long.parseLong(params.getOrDefault("project", 0L).toString());
        Boolean incomplete = params.containsKey("incomplete");

        if (!projectId.equals(0L) && !roleService.hasAnyRoleByProjectId(projectId, ProjectRoleType.VIEWER)) {
            // check if current user has right to see issues of the project
            // if query doesn't specify -> return issues for all project available for user by his role
            issues = Page.empty();
        } else if (roleService.hasAnyRoleByProjectId(projectId, ProjectRoleType.MANAGER)) {
            issues = issueService
                    .findByProjectId(projectId,incomplete, pageable);

        } else {
            issues = issueService
                    .findByUserRole(IssueRoleType.ASSIGNEE, userId, projectId, incomplete, pageable );
        }

        return pagedResourcesAssembler.toModel(issues, assembler);
    }


    /*
    Return an issue if it exists and if User authorized to view the issue.
     */
    @GetMapping("/{issueId}")
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
    @PostMapping
    @PreAuthorize("@RoleService.hasAnyRoleByProjectId(#requestIssue.project.id, @ProjectRole.VIEWER)")
    public ResponseEntity<?> createIssue(@RequestBody IssueModel requestIssue) {
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
    @PutMapping("/{issueId}")
    @PreAuthorize("@RoleService.hasAnyRoleByIssueId(#issueId, @IssueRole.ASSIGNEE)")
    public ResponseEntity<?> updateIssue(@PathVariable Long issueId, @RequestBody IssueModel requestIssue) {
        LOG.debug("PUT Request to update issue with ID: '{}'. New values: '{}'", issueId, requestIssue.toString());
        IssueModel entityModel = assembler.toModel(issueService.update(issueId, requestIssue));
         return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
                 .toUri())
                 .body(entityModel);
    }

    /*
    Update an issue for the project.
    Method wil set fields to NULL (aside from id, name, project and key) if the field was not set in the request body.
    Return 403 if user is not have an ASSIGNEE ROLE or above.
     */
    @PatchMapping("/{issueId}")
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
    @DeleteMapping("/{issueId}")
    public ResponseEntity<?> deleteIssue(@PathVariable Long issueId) {
        LOG.debug("Delete request for project with ID: '{}'", issueId);
        issueService.deleteById(issueId);
        return ResponseEntity.noContent().build();
    }
}
