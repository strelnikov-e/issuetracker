package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.config.RoleService;
import com.strelnikov.issuetracker.entity.*;
import com.strelnikov.issuetracker.exception.AccessForbiddenException;
import com.strelnikov.issuetracker.exception.IssueNotFoundException;
import com.strelnikov.issuetracker.exception.ProjectNotFoundException;
import com.strelnikov.issuetracker.repository.IssueRepository;
import com.strelnikov.issuetracker.repository.IssueRoleRepository;
import com.strelnikov.issuetracker.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class IssueServiceImpl implements IssueService {

	private final IssueRepository issueRepository;
	private final ProjectRepository projectRepository;
	private final IssueRoleRepository issueRoleRepository;
	private final UserService userService;
	private final RoleService roleService;

	public IssueServiceImpl(
			IssueRepository issueRepository,
			ProjectRepository projectRepository,
			IssueRoleRepository issueRoleRepository,
			UserService userService,
			RoleService roleService) {

		this.issueRepository = issueRepository;
		this.projectRepository = projectRepository;
		this.issueRoleRepository = issueRoleRepository;
		this.userService = userService;
		this.roleService = roleService;
	}

	@Override
	public List<Issue> findAll() {
		long userId = userService.getCurrentUser().getId();
		return issueRepository.findAllByUserId(userId);
	}

	@Override
	public List<Issue> findByName(String name) {
		if (name == null || name.equals("")) {
			name = "";
		}
		long userId = userService.getCurrentUser().getId();
		return issueRepository.findByUserIdAndByNameContaining(userId, name);
	}

	@Override
	public List<Issue> findByProjectId(Long projectId) {
		if (!projectRepository.existsById(projectId)) {
			throw new ProjectNotFoundException(projectId);
		}
		long userId = userService.getCurrentUser().getId();
		return issueRepository.findByUserIdAndByProjectId(userId, projectId);
	}

	@Override
	public List<Issue> findByStatus(IssueStatus status) {
		long userId = userService.getCurrentUser().getId();
		return issueRepository.findByUserIdAndByStatus(userId ,status);
	}

	@Override
	public Issue findById(Long issueId) {
		return issueRepository.findById(issueId)
				.orElseThrow(() -> new IssueNotFoundException(issueId));
	}

	@Override
	public Issue save(Issue issue) {
		return issueRepository.save(issue);
	}

	@Override
	@Transactional
	public Issue create(Issue issue) {
		Project project = projectRepository.findById(issue.getProject().getId())
				.orElseThrow(() -> new ProjectNotFoundException(issue.getProject().getId()));
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
		issue.setProject(project);

		if (issue.getKey() == null || issue.getKey().isEmpty()) {
			issue.generateKey(issue.getName());
		}
		Issue savedIssue = issueRepository.save(issue);
		userService.getCurrentUser().addIssueRole(savedIssue, IssueRoleType.REPORTER);
		return savedIssue;
	}

	@Override
	public Issue update(Long issueId, Issue requestIssue) {
		Issue issue = issueRepository.findById(issueId)
				.orElseThrow(() -> new IssueNotFoundException(issueId));
		if (!requestIssue.getName().isEmpty()) {
			issue.setName(requestIssue.getName());
		}
		issue.setDescription(requestIssue.getDescription());
		issue.setParentIssue(requestIssue.getParentIssue());
		issue.setStatus(requestIssue.getStatus());
		issue.setType(requestIssue.getType());
		issue.setPriority(requestIssue.getPriority());
		issue.setTags(requestIssue.getTags());
		issue.setStartDate(requestIssue.getStartDate());
		issue.setDueDate(requestIssue.getDueDate());
		issue.setCloseDate(requestIssue.getCloseDate());

		return issueRepository.save(issue);
	}

	@Override
	public Issue patch(Long issueId, Map<String, Object> fields) {
		Issue issue = issueRepository.findById(issueId)
				.orElseThrow(() -> new IssueNotFoundException(issueId));
		fields.forEach((k, v) -> {
			switch (k) {
				case "name" -> issue.setName(v.toString());
				case "description" -> issue.setDescription(v.toString());
				case "parentIssue" -> issue.setParentIssue((Long) v);
				case "status" -> issue.setStatus(IssueStatus.valueOf(v.toString()));
				case "type" -> issue.setType(IssueType.valueOf(v.toString()));
				case "priority" -> issue.setPriority(IssuePriority.valueOf(v.toString()));
				case "startDate" ->  issue.setStartDate(LocalDate.parse(v.toString()));
				case "dueDate" ->  issue.setDueDate(LocalDate.parse(v.toString()));
				case "closeDate" ->  issue.setCloseDate(LocalDate.parse(v.toString()));
			}
		});
		return issueRepository.save(issue);
	}

	@Override
	@Transactional
	public void deleteById(Long issueId) {
		Issue issue = issueRepository.findById(issueId)
				.orElseThrow(() -> new IssueNotFoundException(issueId));
		if (!roleService.hasAnyRoleByProjectId(issue.getProject().getId(), ProjectRoleType.MANAGER)) {
			throw new AccessForbiddenException();
		};

		issueRoleRepository.deleteAllByIssueId(issueId);
		issueRepository.deleteById(issueId);
	}
}
