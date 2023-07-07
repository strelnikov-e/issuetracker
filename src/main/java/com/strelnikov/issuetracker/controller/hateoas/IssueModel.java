package com.strelnikov.issuetracker.controller.hateoas;

import com.strelnikov.issuetracker.entity.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

public class IssueModel extends RepresentationModel<IssueModel> {

    private Long id;
    private String key;
    private String name;
    private String description;
    private String report;
    private IssueStatus status = IssueStatus.TODO;
    private IssueType type;
    private IssuePriority priority;
    private LocalDate startDate;
    private LocalDate dueDate;
    private LocalDate closeDate;
    private Long parentIssue;
    private Project project;
    private UserModel assignee;
    private UserModel reporter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Long getParentIssue() {
        return parentIssue;
    }

    public void setParentIssue(Long parentIssue) {
        this.parentIssue = parentIssue;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public IssueType getType() {
        return type;
    }

    public void setType(IssueType type) {
        this.type = type;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    public UserModel getAssignee() {
        return assignee;
    }

    public void setAssignee(UserModel assignee) {
        this.assignee = assignee;
    }

    public UserModel getReporter() {
        return reporter;
    }

    public void setReporter(UserModel reporter) {
        this.reporter = reporter;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Issue convertToIssue() {
        Issue issue = new Issue();
        issue.setId(this.id);
        issue.setKey(this.key);
        issue.setName(this.name);
        issue.setDescription(this.description);
        issue.setReport(this.getReport());
        issue.setProject(this.project);
        issue.setParentIssue(this.parentIssue);
        issue.setStatus(this.status);
        issue.setType(this.type);
        issue.setPriority(this.priority);
        issue.setStartDate(this.startDate);
        issue.setDueDate(this.dueDate);
        issue.setCloseDate(this.closeDate);

        return issue;
    }
}
