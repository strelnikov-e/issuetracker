package com.strelnikov.issuetracker.entity;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "issues_roles")
public class IssueRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", updatable = false)
    private Issue issue;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private IssueRoleType type;

    public IssueRole() {
    }

    public IssueRole(User user, Issue issue, IssueRoleType type) {
        this.user = user;
        this.issue = issue;
        this.type = type;
    }

    @Override
    public int hashCode() {
        return this.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }
        IssueRole that = (IssueRole) obj;
        return this.id != null && Objects.equals(this.id, that.id);
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

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public IssueRoleType getType() {
        return type;
    }

    public void setType(IssueRoleType type) {
        this.type = type;
    }
}
