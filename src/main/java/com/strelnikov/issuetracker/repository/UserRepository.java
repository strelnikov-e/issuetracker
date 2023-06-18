package com.strelnikov.issuetracker.repository;

import com.strelnikov.issuetracker.entity.ProjectRoleType;
import com.strelnikov.issuetracker.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByUsernameContaining(String username);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);

    @Query("""
            SELECT user FROM User user
            JOIN ProjectRole proj_role on user.id=proj_role.user.id
            WHERE proj_role.project.id = :id
            """)
    Collection<? extends User> findByProjectId(Long id);

    @Query("""
            SELECT user FROM User user
            JOIN ProjectRole proj_role on user.id=proj_role.user.id
            WHERE proj_role.project.id = :id and proj_role.type = :roleType
            """)
    Collection<? extends User> findViewersByProjectId(Long id, ProjectRoleType roleType);
}
