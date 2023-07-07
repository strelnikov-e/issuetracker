package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.entity.Project;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

public class ProjectModel extends RepresentationModel<ProjectModel> {

    private Long id;
    private String name;
    private String key;
    private String description;
    private LocalDate startDate;
    private String url;
    private List<UserModel> managers;
    private List<UserModel> admins;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<UserModel> getAdmins() {
        return admins;
    }

    public void setAdmins(List<UserModel> admins) {
        this.admins = admins;
    }

    public List<UserModel> getManagers() {
        return managers;
    }

    public void setManagers(List<UserModel> managers) {
        this.managers = managers;
    }

    public Project convertToProject() {
        Project project = new Project();
        project.setId(this.id);
        project.setKey(this.key);
        project.setName(this.name);
        project.setDescription(this.description);
        project.setStartDate(this.startDate);
        project.setUrl(this.url);

        return project;
    }
}
