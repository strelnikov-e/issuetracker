package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.config.RoleService;
import com.strelnikov.issuetracker.controller.hateoas.ProjectModel;
import com.strelnikov.issuetracker.controller.hateoas.UserModel;
import com.strelnikov.issuetracker.entity.*;
import com.strelnikov.issuetracker.exception.exception.ProjectNotFoundException;
import com.strelnikov.issuetracker.repository.ProjectRepository;
import com.strelnikov.issuetracker.repository.ProjectRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final IssueService issueService;
    private final UserService userService;
    private final RoleService roleService;

    public ProjectServiceImpl(
            ProjectRepository projectRepository,
            IssueService issueService,
            UserService userService,
            ProjectRoleRepository projectRoleRepository, RoleService roleService) {

        this.projectRepository = projectRepository;
        this.issueService = issueService;
        this.userService = userService;
        this.projectRoleRepository = projectRoleRepository;
        this.roleService = roleService;
    }

    private User getCurrentUser() {
        Jwt token  = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = token.getClaimAsString("sub");
        return userService.findByEmail(email);
    }

    @Override
    public Page<Project> findAll(Pageable pageable) {
        return projectRepository.findAllByUserId(userService.getCurrentUser().getId(), pageable);
    }

    @Override
    public Page<Project> findByName(String name, Pageable pageable) {
        if (name == null) {
            name = "";
        }
        return projectRepository.findByUserIdAndByNameContaining(getCurrentUser().getId(), name, pageable);
    }

    @Override
    public Project findById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    @Override
    @Transactional
    public Project create(ProjectModel requestProject) {
        // protect against project update
        requestProject.setId(0L);
        // cast Model to project
        Project project = requestProject.convertToProject();
        // generate new project key if it is not provided in request body
        if (project.getKey() == null || project.getKey().isEmpty() || project.getKey().equals("null")) {
            System.out.println("key is null " + project.getKey());
            project.setKey(project.generateKey(project.getName()));
        }
        // save project to DB
        Project savedProject = projectRepository.save(project);
        // assign managers to created project
        getCurrentUser().addProjectRole(savedProject, ProjectRoleType.ADMIN);
        if (requestProject.getManagers() != null && !requestProject.getManagers().isEmpty()) {
            // iterate through list of managers provided in the request body and save roles to ProjectRole table
            for (UserModel manager : requestProject.getManagers()) {
                if (manager.getId() != null && !manager.getId().equals(0L)) {
                    projectRoleRepository.save(new ProjectRole(
                            userService.findById(manager.getId()),
                            savedProject,
                            ProjectRoleType.MANAGER
                    ));
                }
            }
        }
        return savedProject;
    }

    @Override
    @Transactional
    public Project update(ProjectModel requestProject) {
        Project project = projectRepository.findById(requestProject.getId())
                .orElseThrow(() -> new ProjectNotFoundException(requestProject.getId()));
        project.setName(requestProject.getName());
        project.setStartDate(requestProject.getStartDate());
        project.setUrl(requestProject.getUrl());

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
        userService.deleteCurrentProject(projectId);
    }

    @Override
    public boolean existById(Long id) {
        return projectRepository.existsById(id);
    }
}
