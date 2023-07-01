package com.strelnikov.issuetracker.controller.hateoas;

import org.springframework.hateoas.RepresentationModel;

public class UserModel extends RepresentationModel<UserModel> {

    private Long id;
    private String firstName;
    private String lastName;
    private String companyName;
    private Long currentProject;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Long currentProject) {
        this.currentProject = currentProject;
    }
}
