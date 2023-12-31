package com.strelnikov.issuetracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = ALL)
    @JsonIgnore
    private List<ProjectRole> projectRoles = new ArrayList<>();

    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = ALL)
    @JsonIgnore
    private List<IssueRole> issueRoles = new ArrayList<>();

    @Column(columnDefinition = "char(68)")
    private String password;

    private boolean enabled = true;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="company_name")
    private String companyName;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_project", referencedColumnName = "id")
    private Project currentProject;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "users")
    Set<Team> teams = new HashSet<>();


    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, boolean enabled,
                String firstName, String lastName, String companyName) {
        this.email = email;
        this.password = password;
        this.enabled = isEnabled();
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
    }


    public void addIssueRole(Issue issue, IssueRoleType issueRoleType) {
        final var issueRole = new IssueRole(this, issue, issueRoleType);
        issueRole.setIssue(issue);
        issueRole.setRole(issueRoleType);
        issueRole.setUser(this);
        issueRoles.add(issueRole);
    }

    public void addProjectRole(Project project, ProjectRoleType projectRoleType) {
        final var projectRole = new ProjectRole();
        projectRole.setProject(project);
        projectRole.setRole(projectRoleType);
        projectRole.setUser(this);
        projectRoles.add(projectRole);
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public List<ProjectRole> getProjectRoles() {
        return projectRoles;
    }

    public void setProjectRoles(List<ProjectRole> projectRoles) {
        this.projectRoles = projectRoles;
    }

    public List<IssueRole> getIssueRoles() {
        return issueRoles;
    }

    public void setIssueRoles(List<IssueRole> issueRoles) {
        this.issueRoles = issueRoles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = true;
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

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
