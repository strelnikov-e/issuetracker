package com.strelnikov.issuetracker.repository;

import com.strelnikov.issuetracker.entity.IssueRoleType;
import com.strelnikov.issuetracker.entity.Project;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import com.strelnikov.issuetracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Page<User> findByEmailContaining(String email, Pageable pageable);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    @Query("""
            SELECT user FROM User user
            JOIN ProjectRole proj_role on user.id=proj_role.user.id
            WHERE proj_role.project.id = :id
            """)
    List<User> findByProjectId(Long id);

    @Query("""
            SELECT user FROM User user
            JOIN ProjectRole proj_role on user.id=proj_role.user.id
            JOIN IssueRole issue_role on user.id=issue_role.user.id
            WHERE proj_role.project.id = :id AND issue_role.role = :issueRole
            """)
    List<User> findByProjectIdAndIssueRoleType(long id, IssueRoleType issueRole);

    @Query("""
            SELECT user FROM User user
            JOIN ProjectRole proj_role on user.id=proj_role.user.id
            WHERE proj_role.project.id = :id and proj_role.role = :roleType
            """)
    List<User> findViewersByProjectId(Long id, ProjectRoleType roleType, Pageable pageable);

    @Query("""
            SELECT user FROM User user
            JOIN IssueRole issue_role on user.id=issue_role.user.id
            WHERE issue_role.issue.id = :issueId and issue_role.role = :role
            """)
    Optional<User> findByIssueAndRole(Long issueId, IssueRoleType role);

    @Query("""
            SELECT user FROM User user
            JOIN ProjectRole proj_role on user.id=proj_role.user.id
            WHERE proj_role.project.id = :id and proj_role.role = :role
            """)
    Optional<User> findByProjectAndRole(Long id, ProjectRoleType role);

    User findByCurrentProjectId(Long projectId);

    boolean existsByCurrentProjectId(Long projectId);

    @Modifying
    @Query("""
            UPDATE User user
            SET user.currentProject=:project
            WHERE user.id=:id
            """)
    void updateCurrentProjectIdForUserId(Project project, Long id);
}
