package com.strelnikov.issuetracker.config;


import com.strelnikov.issuetracker.entity.*;
import com.strelnikov.issuetracker.exception.IssueNotFoundException;
import com.strelnikov.issuetracker.exception.UserNotFoundException;
import com.strelnikov.issuetracker.repository.*;
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
    private final IssueRepository issueRepository;

    public RoleService(ProjectRoleRepository projectRoleRepository,
                       IssueRoleRepository issueRoleRepository, UserRoleRepository userRoleRepository, UserRepository userRepository, IssueRepository issueRepository) {
        this.projectRoleRepository = projectRoleRepository;
        this.issueRoleRepository = issueRoleRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
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

    @Transactional
    public void changeUserRoleForIssue(Long userId, Long issueId, IssueRoleType role) {
        // fetch user by id
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        // fetch issue by id
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException(issueId));
        IssueRole entry = new IssueRole(user, issue, role);
        // delete previous entry for role and issue
        issueRoleRepository.deleteByIssueIdAndRole(issueId, role);
        // save new entry into the database
        issueRoleRepository.save(entry);
    }

    public void deleteUserRoleForIssue(Long issueId, IssueRoleType role) {
        issueRoleRepository.deleteByIssueIdAndRole(issueId, role);
    }
}


