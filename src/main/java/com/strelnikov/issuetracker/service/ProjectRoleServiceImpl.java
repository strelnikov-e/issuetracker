package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.controller.hateoas.ProjectRoleModel;
import com.strelnikov.issuetracker.entity.Project;
import com.strelnikov.issuetracker.entity.ProjectRole;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.exception.ProjectRoleNotFoundException;
import com.strelnikov.issuetracker.repository.ProjectRoleRepository;
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
    public List<ProjectRole> getAllByProjectId(Long projectId) {

        if (projectId.equals(0L)) {
            User user = userService.getCurrentUser();
            return projectRoleRepository.findAllByUserId(user.getId());
        }
        return projectRoleRepository.findAllByProjectId(projectId);
    }

    @Override
    public ProjectRole getById(Long id) {
        return projectRoleRepository.findById(id)
                .orElseThrow();
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
        fields.forEach((k, v) -> {
            switch (k) {
                case "user" -> projectRole.setUser(userService.findById(Long.parseLong(v.toString())));
                case "project" -> projectRole.setProject(projectService.findById(Long.parseLong(v.toString())));
                case "role" -> projectRole.setRole(ProjectRoleType.valueOf(v.toString()));
            }
        });
    return projectRoleRepository.save(projectRole);
    }
}
