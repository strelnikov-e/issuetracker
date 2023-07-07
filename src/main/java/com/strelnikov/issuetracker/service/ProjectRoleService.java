package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.controller.hateoas.ProjectRoleModel;
import com.strelnikov.issuetracker.entity.ProjectRole;
import com.strelnikov.issuetracker.entity.ProjectRoleType;

import java.util.List;
import java.util.Map;

public interface ProjectRoleService {

    List<ProjectRole> getAllByProjectId(Long projectId);

    ProjectRole getById(Long id);

    ProjectRole patch(Long id, Map<String, Object> fields);

    ProjectRole save(ProjectRoleModel requestUser);

    List<ProjectRole> findByProjectIdAndRole(Long id, ProjectRoleType manager);
}
