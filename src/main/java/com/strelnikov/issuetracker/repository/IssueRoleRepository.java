package com.strelnikov.issuetracker.repository;


import com.strelnikov.issuetracker.entity.IssueRole;
import com.strelnikov.issuetracker.entity.IssueRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface IssueRoleRepository extends JpaRepository<IssueRole, Long> {

    IssueRole findByIssueIdAndRole(Long issueId, IssueRoleType role);

    @Query("""
            SELECT ir.role FROM IssueRole ir
            JOIN ir.issue i
            JOIN i.project p
            WHERE ir.user.id = :userId AND p.id = :projectId
            """)
    Set<IssueRoleType> findRoleTypesByUserIdAndProjectId(Long userId, Long projectId);

    @Query("""
            SELECT ir.role FROM IssueRole ir
            WHERE ir.user.id = :userId AND ir.issue.id = :issueId
            """)
    Set<IssueRoleType> findRoleTypesByUserIdAndIssueId(Long userId, Long issueId);

    void deleteAllByIssueId(Long issueId);

    void deleteByIssueIdAndRole(Long issueId, IssueRoleType role);
}
