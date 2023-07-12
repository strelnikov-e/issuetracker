package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.config.RoleService;
import com.strelnikov.issuetracker.controller.hateoas.ProjectRoleModel;
import com.strelnikov.issuetracker.entity.Project;
import com.strelnikov.issuetracker.entity.ProjectRole;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.exception.exception.AccessForbiddenException;
import com.strelnikov.issuetracker.exception.exception.ProjectRoleNotFoundException;
import com.strelnikov.issuetracker.exception.exception.UserAlreadyAssignedException;
import com.strelnikov.issuetracker.repository.ProjectRoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ProjectRoleServiceImpl implements ProjectRoleService {

    private final ProjectRoleRepository projectRoleRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final RoleService roleService;

    public ProjectRoleServiceImpl(ProjectRoleRepository projectRoleRepository, UserService userService, ProjectService projectService, RoleService roleService) {
        this.projectRoleRepository = projectRoleRepository;
        this.userService = userService;
        this.projectService = projectService;
        this.roleService = roleService;
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
        User user;
        // find user by email or id
        if (requestRoleModel.getUser().getId() == null || requestRoleModel.getUser().getId().equals(0L)) {
            user = userService.findByEmail(requestRoleModel.getUser().getEmail());
        } else {
            user = userService.findById(requestRoleModel.getUser().getId());
        }
        // find project by id
        Project project = projectService.findById(requestRoleModel.getProject().getId());
        ProjectRoleType type = requestRoleModel.getRole();

        // check if user is already assigned to project

        if (projectRoleRepository.existsByUserIdAndProjectId(user.getId(), project.getId())) {
            throw new UserAlreadyAssignedException(user, project.getName());
        }
        // check curren user role against role to assign
        if (roleService.hasAnyRoleByProjectId(project.getId(),ProjectRoleType.ADMIN)) {
            return projectRoleRepository.save(new ProjectRole(user, project, type));
        } else if (roleService.hasAnyRoleByProjectId(project.getId(),ProjectRoleType.MANAGER)
        && !type.equals(ProjectRoleType.ADMIN)) {
            return projectRoleRepository.save(new ProjectRole(user, project, type));
        }
//        ProjectRole role = projectRoleRepository.save(new ProjectRole(user, project, type));
//        user.addProjectRole(project, requestRoleModel.getRole());
        throw new AccessForbiddenException();
    }

    // to implement
    @Override
    public ProjectRole patchUser(Long roleId, Long id) {
        User user = userService.findById(id);

        return null;
    }

    @Override
    public ProjectRole patchRole(Long roleId, ProjectRoleType role) {

        ProjectRole projectRole = projectRoleRepository.findById(roleId)
                .orElseThrow(() -> new ProjectRoleNotFoundException(roleId));
        if (roleService.hasAnyRoleByProjectId(projectRole.getProject().getId(), ProjectRoleType.ADMIN)) {
            // if current user is admin
            projectRole.setRole(role);
            return projectRoleRepository.save(projectRole);
        } else if (roleService
                .hasAnyRoleByProjectId(projectRole.getProject().getId(), ProjectRoleType.MANAGER)
                && !role.equals(ProjectRoleType.ADMIN) && !projectRole.getRole().equals(ProjectRoleType.ADMIN)) {
            // if current user has role MANAGER
            projectRole.setRole(role);
            return projectRoleRepository.save(projectRole);
        }
        throw new AccessForbiddenException();
    }

    @Override
    @Transactional
    public ProjectRole patch(Long id, Map<String, Object> fields) {

        ProjectRole initialRole = projectRoleRepository.findById(id)
                .orElseThrow(() -> new ProjectRoleNotFoundException(id));
        ProjectRole projectRole = initialRole;
        fields.forEach((String k, Object v) -> {
            switch (k) {
                case "user" -> projectRole.setUser(userService.findById(Long.parseLong(v.toString())));
                case "project" -> projectRole.setProject(projectService.findById(Long.parseLong(v.toString())));
                case "role" -> projectRole.setRole(ProjectRoleType.valueOf(v.toString()));
            }
        });
        User user = projectRole.getUser();
        if (roleService.hasAnyRoleByProjectId(projectRole.getProject().getId(), ProjectRoleType.ADMIN)) {
            // if current user is admin
            return projectRoleRepository.save(projectRole);
        } else if (roleService
                .hasAnyRoleByProjectId(projectRole.getProject().getId(), ProjectRoleType.MANAGER)
                && !initialRole.getRole().equals(ProjectRoleType.ADMIN) && !projectRole.getRole().equals(ProjectRoleType.ADMIN)) {
            // if current user has role MANAGER
            return projectRoleRepository.save(projectRole);
        }

        throw new AccessForbiddenException();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ProjectRole projectRole = projectRoleRepository.findById(id)
                .orElseThrow(() -> new ProjectRoleNotFoundException(id));
        if (roleService.hasAnyRoleByProjectId(projectRole.getProject().getId(), ProjectRoleType.ADMIN)) {
            projectRoleRepository.deleteById(id);
        }
        else if (roleService
                .hasAnyRoleByProjectId(projectRole.getProject().getId(), ProjectRoleType.MANAGER)
                && !projectRole.getRole().equals(ProjectRoleType.ADMIN)) {
            projectRoleRepository.deleteById(id);
        } else {
            throw new AccessForbiddenException();
        }
    }
}
