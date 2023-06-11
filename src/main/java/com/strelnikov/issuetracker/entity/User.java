package com.strelnikov.issuetracker.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
//
//    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = PERSIST)
//    @JsonIgnore
//    private List<ProjectRole> projectRoles = new ArrayList<>();
//
//    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = PERSIST)
//    @JsonIgnore
//    private List<IssueRole> issueRoles = new ArrayList<>();

    private String email;

    @Column
    private String password;

    private String authorities;

    private boolean enabled = true;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="company_name")
    private String companyName;


    public User() {

    }

    public User(String username, String password, String authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public User(String username, String email, String password, boolean enabled,
                String firstName, String lastName, String companyName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = true;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
    }


//    public void addIssueRole(Issue issue, IssueRoleType issueRoleType) {
//        final var issueRole = new IssueRole();
//        issueRole.setIssue(issue);
//        issueRole.setType(issueRoleType);
//        issueRole.setUser(this);
//        issueRoles.add(issueRole);
//    }
//
//    public void addProjectRole(Project project, ProjectRoleType projectRoleType) {
//        final var projectRole = new ProjectRole();
//        projectRole.setProject(project);
//        projectRole.setType(projectRoleType);
//        projectRole.setUser(this);
//        projectRoles.add(projectRole);
//    }

//    public List<ProjectRole> getProjectRoles() {
//        return projectRoles;
//    }
//
//    public void setProjectRoles(List<ProjectRole> projectRoles) {
//        this.projectRoles = projectRoles;
//    }
//
//    public List<IssueRole> getIssueRoles() {
//        return issueRoles;
//    }
//
//    public void setIssueRoles(List<IssueRole> issueRoles) {
//        this.issueRoles = issueRoles;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", authorities='" + authorities + '\'' +
                ", enabled=" + enabled +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
