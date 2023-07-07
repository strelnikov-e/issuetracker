package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.controller.hateoas.ProjectRoleModel;
import com.strelnikov.issuetracker.entity.Project;
import com.strelnikov.issuetracker.entity.ProjectRole;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.exception.ProjectRoleNotFoundException;
import com.strelnikov.issuetracker.repository.ProjectRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectRoleServiceImpl implements ProjectRoleService {

    private final ProjectRoleRepository projectRoleRepository;
    private final UserService userService;
    private final ProjectService projectService;

    public ProjectRoleServiceImpl(ProjectRoleRepository projectRoleRepository, UserService userService, ProjectService projectService) {
        this.projectRoleRepository = projectRoleRepository;
        this.userService = userService;
        this.projectService = projectService;
    }

    @Override
    public Page<ProjectRole> getAllByProjectId(Long projectId, Pageable pageable) {

        if (projectId.equals(0L)) {
            User user = userService.getCurrentUser();
            return projectRoleRepository.findAllByUserId(user.getId(), pageable);
        }
        return projectRoleRepository.findAllByProjectId(projectId, pageable);
    }

    @Override
    public ProjectRole getById(Long id) {
        return projectRoleRepository.findById(id)
                .orElseThrow(() -> new ProjectRoleNotFoundException(id));
    }

    @Override
    public List<ProjectRole> findByProjectIdAndRole(Long id, ProjectRoleType role) {
        return projectRoleRepository.findByProjectIdAndRole(id, role);
    }

    @Override
    public ProjectRole save(ProjectRoleModel requestRoleModel) {
        User user = userService.findById(requestRoleModel.getUser().getId());
        Project project = projectService.findById(requestRoleModel.getProject().getId());
        ProjectRoleType type = requestRoleModel.getRole();

        if (projectRoleRepository.existsByUserIdAndProjectIdAndRole(requestRoleModel.getUser().getId(),
                requestRoleModel.getProject().getId(),
                requestRoleModel.getRole())) {
            return projectRoleRepository.findByUserIdAndProjectIdAndRole(
                    requestRoleModel.getUser().getId(),
                    requestRoleModel.getProject().getId(),
                    requestRoleModel.getRole());
        }

        return projectRoleRepository.save(new ProjectRole(user, project, type));
    }

    @Override
    public ProjectRole patch(Long id, Map<String, Object> fields) {
        ProjectRole projectRole = projectRoleRepository.findById(id)
                .orElseThrow(() -> new ProjectRoleNotFoundException(id));
        fields.forEach((String k, Object v) -> {
            switch (k) {
                case "user" -> projectRole.setUser(userService.findById(Long.parseLong(v.toString())));
                case "project" -> projectRole.setProject(projectService.findById(Long.parseLong(v.toString())));
                case "role" -> projectRole.setRole(ProjectRoleType.valueOf(v.toString()));
            }
        });
        ProjectRole saved = projectRoleRepository.save(projectRole);
        System.out.println("Role to save: " + projectRole + "\n" + "Saved role: " + saved);
        return saved;
    }
}
