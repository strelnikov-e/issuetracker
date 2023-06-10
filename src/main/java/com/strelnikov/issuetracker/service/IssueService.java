package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.IssueStatus;

import java.util.List;
import java.util.Map;

public interface IssueService {

    Issue save(Issue issue);

    void deleteById(Long issueId);

    Issue create(Issue issue);

    Issue update(Long issueId, Issue requestIssue);

    Issue findById(Long issueId);

    List<Issue> findByName(String name);

    List<Issue> findByProjectId(Long projectId);

    Issue patch(Long issueId, Map<String, Object> fields);

    List<Issue> findAll();

    List<Issue> findByStatus(IssueStatus status);
}
