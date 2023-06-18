package com.strelnikov.issuetracker.repository;


import com.strelnikov.issuetracker.entity.ProjectRole;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long> {
    @Query("""
        SELECT pr.type FROM ProjectRole pr
        WHERE pr.user.id = :userId AND pr.project.id = :projectId
        """)
    Set<ProjectRoleType> findRoleTypesByUserIdAndProjectId(Long userId, Long projectId);

    @Query("""
        SELECT pr.type FROM ProjectRole pr
        JOIN pr.project p
        JOIN p.issues i
        WHERE pr.user.id = :userId AND i.id = :issueId
        """)
    Set<ProjectRoleType> findRoleTypesByUserIdAndIssueId(Long userId, Long issueId);

    void deleteAllByProjectId(Long projectId);
}
