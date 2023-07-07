package com.strelnikov.issuetracker.repository;


import com.strelnikov.issuetracker.entity.ProjectRole;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long> {
    @Query("""
        SELECT pr.role FROM ProjectRole pr
        WHERE pr.user.id = :userId AND pr.project.id = :projectId
        """)
    Set<ProjectRoleType> findRoleTypesByUserIdAndProjectId(Long userId, Long projectId);

    @Query("""
        SELECT pr.role FROM ProjectRole pr
        JOIN pr.project p
        JOIN p.issues i
        WHERE pr.user.id = :userId AND i.id = :issueId
        """)
    Set<ProjectRoleType> findRoleTypesByUserIdAndIssueId(Long userId, Long issueId);

    void deleteAllByProjectId(Long projectId);

    void deleteByProjectIdAndRole(Long projectId, ProjectRoleType role);

    List<ProjectRole> findByProjectIdAndRole(Long projectId, ProjectRoleType role);

    Page<ProjectRole> findAllByUserId(Long id, Pageable pageable);

    Page<ProjectRole> findAllByProjectId(Long projectId, Pageable pageable);

    ProjectRole findByUserIdAndProjectIdAndRole(Long id, Long id1, ProjectRoleType role);

    boolean existsByUserIdAndProjectIdAndRole(Long id, Long id1, ProjectRoleType role);
}
