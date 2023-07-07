package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.entity.Project;
import org.springframework.hateoas.RepresentationModel;

public class UserModel extends RepresentationModel<UserModel> {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String companyName;
    private Project currentProject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }
}
