package com.strelnikov.issuetracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name="projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column(name="project_key")
    private String key;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    @JsonIgnore
    private List<Issue> issues = new ArrayList<>();

    @Column(name = "description")
    private String description;

    @Column(name = "manager")
    private Long manager;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name="start_date")
    private LocalDate startDate;

    @Column
    private String url;

    @Column(name = "issue_count")
    private Integer issueCount = 0;

    public Project() {
    }

    public Project(String name, String key, List<Issue> issues, String description,
                   Long manager, LocalDate startDate, String url) {
        this.name = name;
        this.issues = issues;
        this.description = description;
        this.manager = manager;
        this.startDate = startDate;
        this.url = url;
        this.key = key;
    }

    public String generateKey(String name) {
        return Arrays.stream(name.split(" "))
                .reduce("",(accumulator, word) -> accumulator + word.charAt(0))
                .toUpperCase();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
            return false;
        }
        Project that = (Project) obj;
        return this.id != 0L && Objects.equals(this.id, that.id);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", manager=" + manager +
                ", startDate=" + startDate +
                ", url='" + url + '\'' +
                '}';
    }

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

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getManager() {
        return manager;
    }

    public void setManager(Long manager) {
        this.manager = manager;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(Integer issueCount) {
        if (issueCount != null) {
            this.issueCount = issueCount;
        }
    }

    public int increaseIssueCounter() {
        return ++this.issueCount;
    }
//
//    public Set<IssueStatus> getIssueStatuses() {
//        return issueStatuses;
//    }
//
//    public void setIssueStatuses(Set<IssueStatus> issueStatuses) {
//        this.issueStatuses = issueStatuses;
//    }
//
//    public boolean addIssueStatus(IssueStatus status) {
//           return this.issueStatuses.add(status);
//    }
//
//    public boolean removeIssueStatus(IssueStatus status) {
//        return this.issueStatuses.remove(status);
//    }
}

