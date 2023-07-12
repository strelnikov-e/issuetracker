package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.controller.hateoas.IssueModel;
import com.strelnikov.issuetracker.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IssueService {

    Issue save(Issue issue);

    void deleteById(Long issueId);

    Issue create(IssueModel issue);

    Issue update(Long issueId, IssueModel requestIssue);

    Issue findById(Long issueId);

    Page<Issue> findByProjectId(Long projectId, Boolean incomplete, Pageable pageable);

    Issue patch(Long issueId, Map<String, Object> fields);

    Page<Issue> findAll(Pageable pageable);

    Page<Issue> findByName(String name, Long projectId, Boolean incomplete, Pageable pageable);

    List<Issue> findByProjectId(Long projectId);

    Page<Issue> findByStatus(IssueStatus status, Long projectId, Pageable pageable);

    Page<Issue> findByPriority(IssuePriority priority, Long projectId, Boolean incomplete, Pageable pageable);

    Page<Issue> findByType(IssueType type, Long projectId, Boolean incomplete, Pageable pageable);

    Page<Issue> findByUserRole(IssueRoleType role, Long assignee, Long projectId, Boolean incomplete, Pageable pageable);

    List<Issue> findByUserRole(IssueRoleType role, Long projectId);

    Page<Issue> findBeforeDueDate(LocalDate dueDate, Long projectId, Boolean incomplete, Pageable pageable);

}
