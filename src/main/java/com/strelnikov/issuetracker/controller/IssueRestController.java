package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.IssueStatus;
import com.strelnikov.issuetracker.service.IssueService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
//@CrossOrigin("http://localhost:3000")
public class IssueRestController {

    private final IssueService issueService;
    private final IssueModelAssembler assembler;
    private final IssueStatusModelAssembler issueStatusModelAssembler;

    public IssueRestController(IssueService issueService, IssueModelAssembler assembler, IssueStatusModelAssembler issueStatusModelAssembler) {
        this.issueService = issueService;
        this.assembler = assembler;
        this.issueStatusModelAssembler = issueStatusModelAssembler;
    }

    // find all issues for current user with optional projectId and name
//    @GetMapping("/issues")
//    public CollectionModel<EntityModel<Issue>> all(
//            @RequestParam(value = "projectId",defaultValue = "0") Long projectId,
//            @RequestParam(value = "name", defaultValue = "") String name) {
//
//        List<EntityModel<Issue>> issues = issueService.findByName(name).stream()
//                .map(assembler::toModel)
//                .collect(Collectors.toList());
//        issues.forEach(System.out::println);
//        if (projectId != 0L) {
//            issues = issues.stream()
//                    .filter(issue -> Objects.requireNonNull(issue.getContent()).getProject().getId() == projectId)
//                    .collect(Collectors.toList());
//        }
//        return CollectionModel
//                .of(issues, linkTo(methodOn(IssueRestController.class)
//                        .all(0L, name)).withSelfRel());
//    }

    @GetMapping("/issues")
    public CollectionModel<EntityModel<?>> all(@RequestParam Map<String, Object> params) {
        List<EntityModel<?>> issues = new ArrayList<>();
        if (params == null || params.isEmpty()) {
            issues = issueService.findAll().stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());;
        } else {
            for (var param: params.entrySet()) {
                switch (param.getKey()) {
                    case "name" -> issues = issueService.findByName(param.getValue().toString())
                            .stream()
                            .map(assembler::toModel)
                            .collect(Collectors.toList());
                    case "projectId" -> issues = issueService.findByProjectId(Long.parseLong(param.getValue().toString()))
                            .stream()
                            .map(assembler::toModel)
                            .collect(Collectors.toList());
                    case "status" -> issues = issueService.findByStatus(IssueStatus.valueOf(param.getValue().toString()))
                            .stream()
                            .map(assembler::toModel)
                            .collect(Collectors.toList());
                }
            }
        }
        return CollectionModel
                .of(issues, linkTo(methodOn(IssueRestController.class)
                        .all(params)).withSelfRel());
    }

    // get issue by issueId, return 403 if user is not authorized to see issue
    @GetMapping("/issues/{issueId}")
    public EntityModel<Issue> getById(@PathVariable Long issueId) {
        Issue issue = issueService.findById(issueId);
        return assembler.toModel(issue) ;
    }

    /*
    create new issue for the project, return 403 if user is not authorized to see project.
    required fields: issue.name, project.id
     */
    @PostMapping("/issues")
    public ResponseEntity<?> createIssue(@RequestBody Issue requestIssue) {
        System.out.println(requestIssue);
        EntityModel<Issue> entityModel = assembler.toModel(issueService.create(requestIssue));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    // update issue, return 403 if user is not authorized to see project
    @PutMapping("/issues/{issueId}")
    public ResponseEntity<?> updateIssue(@PathVariable Long issueId, @RequestBody Issue requestIssue) {
        EntityModel<Issue> entityModel = assembler.toModel(issueService.update(issueId,requestIssue));
         return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
                 .toUri())
                 .body(entityModel);
    }

    @PatchMapping("/issues/{issueId}")
    public ResponseEntity<?> patchIssue(@PathVariable Long issueId, @RequestBody Map<String, Object> fields) {
        if (issueId.compareTo(0L) < 1 || fields == null || fields.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        EntityModel<Issue> entityModel = assembler.toModel(issueService.patch(issueId, fields));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
                .toUri())
                .body(entityModel);
    }

    // Delete an issue and and entry(s) in issue_role table
    // * IMPLEMENT: delete associated tags if orphaned
    @DeleteMapping("/issues/{issueId}")
    public ResponseEntity<?> deleteIssue(@PathVariable Long issueId) {
        issueService.deleteById(issueId);
        return ResponseEntity.noContent().build();
    }
}
