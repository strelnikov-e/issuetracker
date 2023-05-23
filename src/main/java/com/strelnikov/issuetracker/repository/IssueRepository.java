package com.strelnikov.issuetracker.repository;

import com.strelnikov.issuetracker.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
	
	List<Issue> findAllByProjectId(Long projectId);

    List<Issue> findIssuesByTagsId(Long tagId);

    List<Issue> findAllByProjectIdAndNameContaining(Long projectId, String name);

    Collection<Issue> findByProjectIdAndNameContaining(Long projectId, String name);


    List<Issue> findByNameContaining(String name);
}
