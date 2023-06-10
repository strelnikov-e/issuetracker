package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.entity.*;
import com.strelnikov.issuetracker.exception.IssueNotFoundException;
import com.strelnikov.issuetracker.exception.ProjectNotFoundException;
import com.strelnikov.issuetracker.repository.IssueRepository;
import com.strelnikov.issuetracker.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class IssueServiceImpl implements IssueService {

	IssueRepository issueRepository;
	ProjectRepository projectRepository;

	public IssueServiceImpl(IssueRepository issueRepository, ProjectRepository projectRepository) {
		this.issueRepository = issueRepository;
		this.projectRepository = projectRepository;
	}

	@Override
	public List<Issue> findAll() {
		return issueRepository.findAll();
	}

	@Override
	public Issue save(Issue issue) {
		return issueRepository.save(issue);
	}

	@Override
	public List<Issue> findByName(String name) {
		if (name == null || name.equals("")) {
			name = "";
		}
		return issueRepository.findByNameContaining(name);
	}

	@Override
	public List<Issue> findByProjectId(Long projectId) {
		if (!projectRepository.existsById(projectId)) {
			throw new ProjectNotFoundException(projectId);
		}
		return issueRepository.findAllByProjectId(projectId);
	}

	@Override
	public Issue findById(Long issueId) {
		return issueRepository.findById(issueId).orElseThrow(() -> new IssueNotFoundException(issueId));
	}

	@Override
	public List<Issue> findByStatus(IssueStatus status) {
		return issueRepository.findByStatus(status);
	}

	@Override
	public Issue create(Issue issue) {
		issue.setId(0L);
		if (issue.getStatus() == null) {
			issue.setStatus(IssueStatus.TODO);
		}
		if (issue.getPriority() == null) {
			issue.setPriority(IssuePriority.MEDIUM);
		}
		if (issue.getType() == null) {
			issue.setType(IssueType.TASK);
		}

		Project project = projectRepository.findById(issue.getProject().getId())
				.orElseThrow(() ->new ProjectNotFoundException(issue.getProject().getId()));
		issue.setProject(project);
		if (issue.getKey() == null || issue.getKey().isEmpty()) {
			issue.generateKey(issue.getName());
		}
		return issueRepository.save(issue);
	}

	@Override
	public Issue update(Long issueId, Issue requestIssue) {
		Issue issue = issueRepository.findById(issueId)
				.orElseThrow(() -> new IssueNotFoundException(issueId));
		issue.setTags(requestIssue.getTags());
		issue.setAssignee(requestIssue.getAssignee());
		issue.setDescription(requestIssue.getDescription());
		issue.setName(requestIssue.getName());
		issue.setStatus(requestIssue.getStatus());
		issue.setStartDate(requestIssue.getStartDate());
		return issueRepository.save(issue);
	}

	@Override
	public Issue patch(Long issueId, Map<String, Object> fields) {
		Issue issue = issueRepository.findById(issueId)
				.orElseThrow(() -> new IssueNotFoundException(issueId));
		fields.remove("id");
		fields.forEach((k, v) -> {
			Field field = ReflectionUtils.findField(Issue.class, k);
			field.setAccessible(true);
			switch (k) {
				case "status" -> v = IssueStatus.valueOf(v.toString());
				case "priority" -> v = IssuePriority.valueOf(v.toString());
				case "type" -> v = IssueType.valueOf(v.toString());
				case "closeDate" -> v = LocalDate.parse(v.toString());
			}
			ReflectionUtils.setField(field, issue, v);
		});
		return issueRepository.save(issue);
	}

	@Override
	@Transactional
	public void deleteById(Long issueId) {
		if (!issueRepository.existsById(issueId)) {
			throw new IssueNotFoundException(issueId);
		}
		issueRepository.deleteById(issueId);
	}
}
