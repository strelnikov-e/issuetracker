package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.entity.ProjectRoleType;
import org.springframework.hateoas.RepresentationModel;

public class ProjectRoleModel extends RepresentationModel<ProjectRoleModel> {

    private Long id;
    private UserModel user;
    private ProjectModel project;
    private ProjectRoleType role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public ProjectModel getProject() {
        return project;
    }

    public void setProject(ProjectModel project) {
        this.project = project;
    }

    public ProjectRoleType getRole() {
        return role;
    }

    public void setRole(ProjectRoleType role) {
        this.role = role;
    }
}
