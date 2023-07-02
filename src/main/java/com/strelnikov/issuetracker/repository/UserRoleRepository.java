package com.strelnikov.issuetracker.repository;


import com.strelnikov.issuetracker.entity.UserRole;
import com.strelnikov.issuetracker.entity.UserRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("""
    SELECT user_role.role FROM UserRole user_role
    WHERE user_role.user.id = :userId
    """)
    Set<UserRoleType> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
