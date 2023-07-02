package com.strelnikov.issuetracker.repository;

import com.strelnikov.issuetracker.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("""
            SELECT issue from Issue issue
            JOIN Project proj ON issue.project.id = proj.id
            JOIN ProjectRole proj_role ON proj.id = proj_role.project.id
            WHERE proj_role.user.id = :userId
            """)
    Page<Issue> findAllByUserId(long userId, Pageable pageable);

    // to only show issues of projects where user has any role
    @Query("""
            SELECT issue from Issue issue
            JOIN Project proj ON issue.project.id = proj.id
            JOIN ProjectRole proj_role ON proj.id = proj_role.project.id
            WHERE proj_role.user.id = :userId AND issue.name LIKE %:name%
            """)
    Page<Issue> findByUserIdAndByNameContaining(Long userId, String name, Pageable pageable);

    Page<Issue> findByProjectIdAndNameContaining(Long projectId, String name, Pageable pageable);

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
            WHERE proj_role.user.id = :userId AND issue.project.id=:projectId
            """)
    Page<Issue> findByUserIdAndByProjectId(long userId, Long projectId, Pageable pageable);

    @Query("""
            SELECT issue from Issue issue
            JOIN Project proj ON issue.project.id = proj.id
            JOIN ProjectRole proj_role ON proj.id = proj_role.project.id
            WHERE proj_role.user.id = :userId AND issue.status LIKE :status
            """)
    Page<Issue> findByUserIdAndByStatus(long userId, IssueStatus status, Pageable pageable);

    Page<Issue> findByProjectIdAndStatus(Long projectId, IssueStatus status, Pageable pageable);

    @Query("""
            SELECT issue from Issue issue
            JOIN Project proj ON issue.project.id = proj.id
            JOIN ProjectRole proj_role ON proj.id = proj_role.project.id
            WHERE proj_role.user.id = :userId AND issue.priority LIKE :priority
            """)
    Page<Issue> findByUserIdAndByPriority(long userId, IssuePriority priority, Pageable pageable);

    Page<Issue> findByProjectIdAndPriority(Long projectId, IssuePriority priority, Pageable pageable);
    
    @Query("""
            SELECT issue from Issue issue
            JOIN Project proj ON issue.project.id = proj.id
            JOIN ProjectRole proj_role ON proj.id = proj_role.project.id
            WHERE proj_role.user.id = :userId AND issue.type LIKE :type
            """)
    Page<Issue> findByUserIdAndByType(long userId, IssueType type, Pageable pageable);

    Page<Issue> findByProjectIdAndType(Long projectId, IssueType type, Pageable pageable);

    Page<Issue> findAllByProjectId(Long projectId, Pageable pageable);

    @Query("""
            SELECT issue from Issue issue
            JOIN IssueRole issue_role ON issue.id = issue_role.issue.id
            JOIN Project proj ON issue.project.id = proj.id
            JOIN ProjectRole proj_role ON proj.id = proj_role.project.id
            WHERE proj_role.user.id = :currentUserId AND issue_role.role = :role AND issue_role.user.id = :assignee
            """)
    Page<Issue> findByUserIdAndByAssignee(IssueRoleType role, Long assignee, long currentUserId, Pageable pageable);

    @Query("""
            SELECT issue from Issue issue
            JOIN IssueRole issue_role ON issue.id = issue_role.issue.id
            WHERE issue_role.role = :role AND issue_role.user.id = :assignee AND issue.project.id = :projectId
            """)
    Page<Issue> findByProjectIdAndByAssignee(IssueRoleType role, Long assignee, Long projectId, Pageable pageable);
    
    @Query("""
            SELECT issue from Issue issue
            JOIN Project proj ON issue.project.id = proj.id
            JOIN ProjectRole proj_role ON proj.id = proj_role.project.id
            WHERE proj_role.user.id = :userId AND issue.dueDate < :dueDate AND issue.status <> DONE
            """)
    Page<Issue> findByUserIdAndDueDateLessThan(long userId, LocalDate dueDate, Pageable pageable);


    Page<Issue> findByProjectIdAndDueDateLessThanAndStatusNot(Long projectId, LocalDate dueDate, IssueStatus status, Pageable pageable);
}
