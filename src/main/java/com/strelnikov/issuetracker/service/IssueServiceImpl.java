package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.config.RoleService;
import com.strelnikov.issuetracker.controller.hateoas.IssueModel;
import com.strelnikov.issuetracker.entity.*;
import com.strelnikov.issuetracker.exception.AccessForbiddenException;
import com.strelnikov.issuetracker.exception.IssueNotFoundException;
import com.strelnikov.issuetracker.exception.ProjectNotFoundException;
import com.strelnikov.issuetracker.repository.IssueRepository;
import com.strelnikov.issuetracker.repository.IssueRoleRepository;
import com.strelnikov.issuetracker.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public Page<Issue> findAll(Pageable pageable) {
		long userId = getUserId();
		return issueRepository.findAllByUserId(userId, pageable);
	}

	@Override
	public Page<Issue> findByName(String name, Long projectId, Boolean incomplete, Pageable pageable) {
		if (projectId.equals(0L)) {
			long userId = getUserId();
			if (name == null || name.equals("")) {
				if (incomplete) {
					return issueRepository.findAllByUserIdAndByStatusNot(userId, IssueStatus.DONE, pageable);
				}
				return issueRepository.findAllByUserId(userId, pageable);
			}
			if (incomplete) {
				return issueRepository.findByUserIdAndNameContainingAndStatusNot(userId, name, IssueStatus.DONE, pageable);
			}
			return issueRepository.findByUserIdAndByNameContaining(userId, name, pageable);
		}
		if (incomplete) {
			return issueRepository.findByProjectIdAndNameContainingAndStatusNot(projectId, name, IssueStatus.DONE, pageable);
		}
		return issueRepository.findByProjectIdAndNameContaining(projectId, name, pageable);
	}

	@Override
	public Page<Issue> findByProjectId(Long projectId,Boolean incomplete, Pageable pageable) {
		if (projectId == null || projectId.equals(0L)) {
			long userId = getUserId();
			if (incomplete) {
				return issueRepository.findAllByUserIdAndStatusNot(userId, IssueStatus.DONE, pageable);
			}
			return issueRepository.findAllByUserId(userId, pageable);
		}
		if (incomplete) {
			return issueRepository.findAllByProjectIdAndStatusNot(projectId, IssueStatus.DONE, pageable);
		}
		return issueRepository.findAllByProjectId(projectId, pageable);
	}

	@Override
	public List<Issue> findByProjectId(Long projectId) {
		long userId = getUserId();
		return issueRepository.findByUserIdAndByProjectId(userId, projectId);
	}

	@Override
	public Page<Issue> findByStatus(IssueStatus status, Long projectId, Pageable pageable) {
		if (projectId.equals(0L)) {
			long userId = getUserId();
			return issueRepository.findByUserIdAndByStatus(userId, status, pageable);
		}
		return issueRepository.findByProjectIdAndStatus(projectId, status, pageable);
	}

	@Override
	public Page<Issue> findByPriority(IssuePriority priority, Long projectId, Boolean incomplete, Pageable pageable) {
		if (projectId.equals(0L)) {
			long userId = getUserId();
			if (!incomplete) {
				return issueRepository.findByUserIdAndPriorityAndStatusNot(userId, priority, IssueStatus.DONE, pageable);
			}
			return issueRepository.findByUserIdAndByPriority(userId, priority, pageable);
		}
		if (incomplete) {
			return issueRepository.findByProjectIdAndPriorityAndStatusNot(projectId, priority, IssueStatus.DONE, pageable);
		}
		return issueRepository.findByProjectIdAndPriority(projectId, priority, pageable);
	}

	@Override
	public Page<Issue> findByType(IssueType type, Long projectId, Boolean incomplete, Pageable pageable) {
		if (projectId.equals(0L)) {
			long userId = getUserId();
			if (incomplete) {
				return issueRepository.findByUserIdAndTypeAndStatusNot(userId, type, IssueStatus.DONE, pageable);
			}
			return issueRepository.findByUserIdAndByType(userId, type, pageable);
		}
		if (incomplete){
			return issueRepository.findByProjectIdAndTypeAndStatusNot(projectId, type, IssueStatus.DONE, pageable);
		}
		return issueRepository.findByProjectIdAndType(projectId, type, pageable);
	}

	@Override
	public Issue findById(Long issueId) {
		return issueRepository.findById(issueId)
				.orElseThrow(() -> new IssueNotFoundException(issueId));
	}

	@Override
	public Page<Issue> findByUserRole(IssueRoleType role, Long assignee, Long projectId, Boolean incomplete, Pageable pageable) {
		if (projectId.equals(0L)) {
			long userId = getUserId();
			if (incomplete) {
				return issueRepository.findByUserIdAndAssigneeAndStatusNot(role, assignee, userId, IssueStatus.DONE, pageable);
			}
			return issueRepository.findByUserIdAndByAssignee(role, assignee, userId, pageable);
		}
		if (incomplete) {
			return issueRepository.findByProjectIdAndByAssigneeAndStatusNot(role, assignee, projectId, IssueStatus.DONE, pageable);
		}
		return issueRepository.findByProjectIdAndByAssignee(role, assignee, projectId, pageable);
	}

	@Override
	public Page<Issue> findBeforeDueDate(LocalDate dueDate, Long projectId, Boolean incomplete, Pageable pageable) {
		if (projectId.equals(0L)) {
			long userId = getUserId();
			if (incomplete) {
				return issueRepository.findByUserIdAndDueDateLessThanAndStatusNot(userId, dueDate, IssueStatus.DONE, pageable);
			}
			return issueRepository.findByUserIdAndDueDateLessThan(userId, dueDate, pageable);
		}
		if (incomplete) {
			return issueRepository.findByProjectIdAndDueDateLessThanAndStatusNot(projectId, dueDate, IssueStatus.DONE, pageable);
		}
		return issueRepository.findByProjectIdAndDueDateLessThan(projectId, dueDate, pageable);

	}

	@Override
	public Issue save(Issue issue) {
		return issueRepository.save(issue);
	}

	@Override
	@Transactional
	public Issue create(IssueModel requestIssue) {
		Issue issue = requestIssue.convertToIssue();
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
		if (requestIssue.getAssignee() != null
				&& requestIssue.getAssignee().getId() != null
				&& !requestIssue.getAssignee().getId().equals(0L)) {
			roleService.changeUserRoleForIssue(
					requestIssue.getAssignee().getId(),
					savedIssue.getId(),
					IssueRoleType.ASSIGNEE);
		}
		return savedIssue;
	}

	@Override
	@Transactional
	public Issue update(Long issueId, IssueModel requestIssue) {
		Issue issue = issueRepository.findById(issueId)
				.orElseThrow(() -> new IssueNotFoundException(issueId));
		if (requestIssue.getName() != null && !requestIssue.getName().isEmpty()) {
			issue.setName(requestIssue.getName());
		}
		issue.setDescription(requestIssue.getDescription());
		issue.setParentIssue(requestIssue.getParentIssue());
		issue.setStatus(requestIssue.getStatus());
		issue.setType(requestIssue.getType());
		issue.setPriority(requestIssue.getPriority());
		issue.setStartDate(requestIssue.getStartDate());
		issue.setDueDate(requestIssue.getDueDate());
		issue.setCloseDate(requestIssue.getCloseDate());

		if (requestIssue.getAssignee() == null
				|| requestIssue.getAssignee().getId() == null
				|| requestIssue.getAssignee().getId().equals(0L)) {
			roleService.deleteUserRoleForIssue(issueId, IssueRoleType.ASSIGNEE);
		} else {
			roleService.changeUserRoleForIssue(
					requestIssue.getAssignee().getId(),
					requestIssue.getId(),
					IssueRoleType.ASSIGNEE);
		}
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
				case "assignee" -> roleService
						.changeUserRoleForIssue(Long.parseLong(v.toString()), issueId, IssueRoleType.ASSIGNEE);
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

	private long getUserId() {
		return userService.getCurrentUser().getId();
	}
}

