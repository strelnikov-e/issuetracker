package com.strelnikov.issuetracker.config;


import com.strelnikov.issuetracker.entity.*;
import com.strelnikov.issuetracker.exception.exception.IssueNotFoundException;
import com.strelnikov.issuetracker.exception.exception.ProjectNotFoundException;
import com.strelnikov.issuetracker.exception.exception.UserNotFoundException;
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
    private final ProjectRepository projectRepository;

    public RoleService(ProjectRoleRepository projectRoleRepository,
                       IssueRoleRepository issueRoleRepository, UserRoleRepository userRoleRepository, UserRepository userRepository, IssueRepository issueRepository, ProjectRepository projectRepository) {
        this.projectRoleRepository = projectRoleRepository;
        this.issueRoleRepository = issueRoleRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.issueRepository = issueRepository;
        this.projectRepository = projectRepository;
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

    // this method showld be used to change assignee userId only! Needs refactoring
    @Transactional
    public void changeUserRoleForIssue(Long userId, Long issueId, IssueRoleType role) {
        // fetch user by id
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        // fetch issue by id
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException(issueId));

        IssueRole issueRole = issueRoleRepository.findByIssueIdAndRole(issueId, IssueRoleType.ASSIGNEE);
        if (issueRole != null && issueRole.getUser().getId().equals(userId)) {
            return;
        }

        deleteUserRoleForIssue(issueId,IssueRoleType.ASSIGNEE);
        if (role == IssueRoleType.ASSIGNEE) {
            IssueRole entry = new IssueRole(user, issue, role);
            issueRoleRepository.saveAndFlush(entry);
        }
//        if (role == null) {
//            issueRoleRepository.deleteByIssueIdAndRole(issueId, IssueRoleType.ASSIGNEE);
//            return;
//        }

//        IssueRole entry = issueRoleRepository.findByIssueIdAndRole(issueId, role);
//        if (entry == null) {
//            entry = new IssueRole(user, issue, role);
//        } else {
//            entry.setUser(user);
//        }

//        issueRoleRepository.save(entry);
    }

    public void addProjectRole(Long userId, Long projectId, ProjectRoleType role) {
        // fetch user by id
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        // fetch project by id
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        // check if required entry in DB already persisted
        if (projectRoleRepository.existsByUserIdAndProjectIdAndRole(userId, projectId, role)) {
            return;
        }

        ProjectRole entry = new ProjectRole(user, project, role);
        projectRoleRepository.save(entry);

    }

    @Transactional
    public void deleteUserRoleForProject(Long projectId, ProjectRoleType role) {
        projectRoleRepository.deleteByProjectIdAndRole(projectId, role);
    }

    @Transactional
    public void deleteUserRoleForIssue(Long issueId, IssueRoleType role) {
        issueRoleRepository.deleteByIssueIdAndRole(issueId, role);
    }


}


