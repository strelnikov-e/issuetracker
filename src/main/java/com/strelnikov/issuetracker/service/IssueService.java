package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.entity.Issue;

import java.util.List;

public interface IssueService {

    Issue save(Issue issue);

    void deleteById(Long issueId);

    Issue create(Issue issue, Long projectId);

    Issue update(Long issueId, Issue requestIssue);

    Issue findById(Long issueId);

    List<Issue> findByName(String name);

    List<Issue> findByProjectId(Long projectId);
}
