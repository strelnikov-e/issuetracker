package com.strelnikov.issuetracker.entity;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "projects_roles")
public class ProjectRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column
    private ProjectRoleType role;

    public ProjectRole() {
    }

    public ProjectRole(User user, Project project, ProjectRoleType role) {
        this.user = user;
        this.project = project;
        this.role = role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }
        ProjectRole that = (ProjectRole) obj;
        return this.id != 0L && Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ProjectRoleType getRole() {
        return role;
    }

    public void setRole(ProjectRoleType role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "ProjectRole{" +
                "id=" + id +
                ", user=" + user +
                ", project=" + project +
                ", role=" + role +
                '}';
    }
}
