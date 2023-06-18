package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.Project;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.exception.ProjectNotFoundException;
import com.strelnikov.issuetracker.repository.ProjectRepository;
import com.strelnikov.issuetracker.repository.ProjectRoleRepository;
import com.strelnikov.issuetracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final IssueService issueService;
    private final UserRepository userRepository;

    public ProjectServiceImpl(
            ProjectRepository projectRepository,
            IssueService issueService,
            UserRepository userRepository,
            ProjectRoleRepository projectRoleRepository) {

        this.projectRepository = projectRepository;
        this.issueService = issueService;
        this.userRepository = userRepository;
        this.projectRoleRepository = projectRoleRepository;
    }

    private User getCurrentUser() {
        Jwt token  = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = token.getClaimAsString("sub");
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public List<Project> findAll() {
        return projectRepository.findAllByUserId(getCurrentUser().getId());
    }

    @Override
    public List<Project> findByName(String name) {
        if (name == null) {
            name = "";
        }
        return projectRepository.findByUserIdAndByNameContaining(getCurrentUser().getId(), name);
    }

    @Override
    public Project findById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    @Override
    @Transactional
    public Project create(Project project) {
        project.setId(0L);
        if (project.getKey() == null || project.getKey().isEmpty()) {
            System.out.println("ket is null " + project.getKey());
            project.setKey(project.generateKey(project.getName()));
        }
        Project savedProject = projectRepository.save(project);
        getCurrentUser().addProjectRole(savedProject, ProjectRoleType.ADMIN);
        return savedProject;
    }

    @Override
    @Transactional
    public Project update(Project project) {
        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public void deleteById(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }
        List<Issue> issues = issueService.findByProjectId(projectId);
        for (Issue issue : issues) {
            issueService.deleteById(issue.getId());
        }
        projectRoleRepository.deleteAllByProjectId(projectId);
        projectRepository.deleteById(projectId);
    }
}
