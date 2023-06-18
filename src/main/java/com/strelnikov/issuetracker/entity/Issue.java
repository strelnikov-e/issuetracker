package com.strelnikov.issuetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="issues")
public class Issue {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@NotNull()
	@NotBlank(message = "name field cannot be empty")
	private String name;

	@Column(name = "issue_key")
	private String key;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "project_id")
	private Project project;

	private String description;

	@Column(name="parent_issue")
	private Long parentIssue;

	@Column(columnDefinition = "char")
	private IssueStatus status = IssueStatus.TODO;

	@Column(columnDefinition = "char")
	private IssueType type;

	@Column(columnDefinition = "char")
	private IssuePriority priority;

	@Column(name="start_date")
	private LocalDate startDate;

	@Column(name="due_date")
	private LocalDate dueDate;

	@Column(name="close_date")
	private LocalDate closeDate;


	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "issues_tags",
			joinColumns = { @JoinColumn(name = "issue_id") },
			inverseJoinColumns = { @JoinColumn(name = "tag_id") })
	private Set<Tag> tags = new HashSet<>();


	public Issue() {
	}

	public Issue(String name, Project project, String description, Long parentIssue,
				 IssueStatus status, IssueType type, IssuePriority priority,
				 LocalDate startDate, LocalDate dueDate, LocalDate closeDate, String key) {
		this.name = name;
		this.project = project;
		this.description = description;
		this.parentIssue = parentIssue;
		this.status = status;
		this.type = type;
		this.priority = priority;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.closeDate = closeDate;
		generateKey(name);
	}

	public void generateKey(String name) {
		this.key = project.getKey() + "-" + project.increaseIssueCounter();
	}

	public void removeTag(Long tagId) {
		Tag tag = this.tags.stream()
				.filter(t -> Objects.equals(t.getId(), tagId))
				.findFirst()
				.orElse(null);
		if (tag != null) {
			this.tags.remove(tag);
			tag.getIssues().remove(this);
		}
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
		tag.getIssues().add(this);
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
		Issue that = (Issue) obj;
		return this.id != 0L && Objects.equals(this.id, that.id);
	}

	@Override
	public String toString() {
		return "Issue{" +
				"id=" + id +
				", name='" + name +
				", project=" + project +
				", description='" + description +
				", status=" + status +
				", type=" + type +
				", priority=" + priority +
				", startDate=" + startDate +
				", dueDate=" + dueDate +
				", closeDate=" + closeDate +
				", key='" + key  +
				", tags=" + tags +
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public void setStatus(IssueStatus issueStatus) {
		this.status = issueStatus;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
}
