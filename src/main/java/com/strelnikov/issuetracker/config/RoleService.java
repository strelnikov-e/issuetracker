package com.strelnikov.issuetracker.config;


import com.strelnikov.issuetracker.entity.*;
import com.strelnikov.issuetracker.repository.IssueRoleRepository;
import com.strelnikov.issuetracker.repository.ProjectRoleRepository;
import com.strelnikov.issuetracker.repository.UserRepository;
import com.strelnikov.issuetracker.repository.UserRoleRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service("RoleService")
public class RoleService {

    private final ProjectRoleRepository projectRoleRepository;
    private final IssueRoleRepository issueRoleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    public RoleService(ProjectRoleRepository projectRoleRepository,
                       IssueRoleRepository issueRoleRepository, UserRoleRepository userRoleRepository, UserRepository userRepository) {
        this.projectRoleRepository = projectRoleRepository;
        this.issueRoleRepository = issueRoleRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean hasAnyRoleByProjectId(Long projectId, Role... roles) {
        final Long userId = getCurrentUser().getId();
        final Set<ProjectRoleType> projectRoleTypes = projectRoleRepository.findRoleTypesByUserIdAndProjectId(userId, projectId);
        for (Role role : roles) {
            if (projectRoleTypes.stream().anyMatch(projectRoleType -> projectRoleType.includes(role))) {
                return true;
            }
        }
        final Set<IssueRoleType> issueRoleTypes = issueRoleRepository.findRoleTypesByUserIdAndProjectId(userId, projectId);
        for (Role role : roles) {
            if (issueRoleTypes.stream().anyMatch(issueRoleType -> issueRoleType.includes(role))) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean hasAnyRoleByIssueId(Long issueId, Role... roles) {
        final Long userId = getCurrentUser().getId();
        final Set<ProjectRoleType> projectRoleTypes = projectRoleRepository.findRoleTypesByUserIdAndIssueId(userId, issueId);
        for (Role role : roles) {
            if (projectRoleTypes.stream().anyMatch(projectRoleType -> projectRoleType.includes(role))) {
                return true;
            }
        }
        final Set<IssueRoleType> issueRoleTypes = issueRoleRepository.findRoleTypesByUserIdAndIssueId(userId, issueId);
        for (Role role : roles) {
            if (issueRoleTypes.stream().anyMatch(issueRoleType -> issueRoleType.includes(role))) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean hasRootRole() {
        final Long userId = getCurrentUser().getId();
        final Set<UserRoleType> userRoleTypes = userRoleRepository.findAllByUserId(userId);
        for (Role role : userRoleTypes) {
            if (role == UserRoleType.ROOT) return true;
        }
        return false;
    }

    public User getCurrentUser() {
        Jwt token  = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = token.getClaimAsString("sub");
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}


