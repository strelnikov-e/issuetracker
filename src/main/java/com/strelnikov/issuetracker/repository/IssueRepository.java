package com.strelnikov.issuetracker.repository;

import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("""
            SELECT issue from Issue issue
            JOIN Project proj ON issue.project.id = proj.id
            JOIN ProjectRole proj_role ON proj.id = proj_role.project.id
            WHERE proj_role.user.id = :userId
            """)
    List<Issue> findAllByUserId(long userId);

    @Query("""
            SELECT issue from Issue issue
            JOIN Project proj ON issue.project.id = proj.id
            JOIN ProjectRole proj_role ON proj.id = proj_role.project.id
            WHERE proj_role.user.id = :userId AND issue.name LIKE :name
            """)
    List<Issue> findByUserIdAndByNameContaining(Long userId, String name);

    @Query("""
            SELECT issue from Issue issue
            JOIN Project proj ON issue.project.id = proj.id
            JOIN ProjectRole proj_role ON proj.id = proj_role.project.id
            WHERE proj_role.user.id = :userId AND issue.project.id=:projectId
            """)
    List<Issue> findByUserIdAndByProjectId(long userId, Long projectId);

    @Query("""
            SELECT issue from Issue issue
            JOIN Project proj ON issue.project.id = proj.id
            JOIN ProjectRole proj_role ON proj.id = proj_role.project.id
            WHERE proj_role.user.id = :userId AND issue.status LIKE :status
            """)
    List<Issue> findByUserIdAndByStatus(long userId, IssueStatus status);
}
