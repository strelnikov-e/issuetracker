package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.IssueStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IssueService {

    Issue save(Issue issue);

    void deleteById(Long issueId);

    Issue create(Issue issue);

    Issue update(Long issueId, Issue requestIssue);

    Issue findById(Long issueId);

    Page<Issue> findByName(String name, Pageable pageable);

    Page<Issue> findByProjectId(Long projectId, Pageable pageable);

    Issue patch(Long issueId, Map<String, Object> fields);

    Page<Issue> findAll(Pageable pageable);

    Page<Issue> findByStatus(IssueStatus status, Pageable pageable);

    List<Issue> findByProjectId(Long projectId);
}
