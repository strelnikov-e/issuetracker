package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.Project;
import com.strelnikov.issuetracker.exception.IssueNotFoundException;
import com.strelnikov.issuetracker.exception.ProjectNotFoundException;
import com.strelnikov.issuetracker.repository.IssueRepository;
import com.strelnikov.issuetracker.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IssueServiceImpl implements IssueService {

	IssueRepository issueRepository;
	ProjectRepository projectRepository;

	public IssueServiceImpl(IssueRepository issueRepository, ProjectRepository projectRepository) {
		this.issueRepository = issueRepository;
		this.projectRepository = projectRepository;
	}

	@Override
	@Transactional
	public void deleteById(Long issueId) {
		if (!issueRepository.existsById(issueId)) {
			throw new IssueNotFoundException(issueId);
		}
		issueRepository.deleteById(issueId);
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
	public Issue create(Issue issue, Long projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() ->new ProjectNotFoundException(projectId));
		issue.setProject(project);
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
		return issueRepository.save(issue);
	}
}
