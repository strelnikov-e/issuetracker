package com.strelnikov.issuetracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "project_id")
	@JsonIgnore
	private Project project;

	private String description;

	private Long assignee;

	@Column(columnDefinition = "char")
	private String status;

	@Column(name="start_date")
	private LocalDate startDate;

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinTable(name = "issues_tags",
			joinColumns = { @JoinColumn(name = "issue_id") },
			inverseJoinColumns = { @JoinColumn(name = "tag_id") })
	private Set<Tag> tags = new HashSet<>();


	public Issue() {
		
	}

	public Issue(String name, Project project, String description, Long assignee, String status, LocalDate startDate) {
		this.name = name;
		this.project = project;
		this.description = description;
		this.assignee = assignee;
		this.status = status;
		this.startDate = startDate;
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

	@java.lang.Override
	public java.lang.String toString() {
		return "Issue{" +
				"id=" + id +
				", name='" + name + '\'' +
				", project=" + project +
				", description='" + description + '\'' +
				", assignee='" + assignee + '\'' +
				", status='" + status + '\'' +
				", startDate=" + startDate +
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

	public Long getAssignee() {
		return assignee;
	}

	public void setAssignee(Long assignee) {
		this.assignee = assignee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
}
