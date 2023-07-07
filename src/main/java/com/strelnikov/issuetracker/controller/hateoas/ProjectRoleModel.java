package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.entity.ProjectRoleType;
import org.springframework.hateoas.RepresentationModel;

public class ProjectRoleModel extends RepresentationModel<ProjectRoleModel> {

    private Long id;
    private ProjectRoleType role;
    private UserShortModel user;
    private ProjectShortModel project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserShortModel getUser() {
        return user;
    }

    public void setUser(UserShortModel user) {
        this.user = user;
    }

    public ProjectShortModel getProject() {
        return project;
    }

    public void setProject(ProjectShortModel project) {
        this.project = project;
    }

    public ProjectRoleType getRole() {
        return role;
    }

    public void setRole(ProjectRoleType role) {
        this.role = role;
    }
}
