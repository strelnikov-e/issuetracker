package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.entity.Issue;
import com.strelnikov.issuetracker.entity.Project;
import com.strelnikov.issuetracker.exception.ProjectNotFoundException;
import com.strelnikov.issuetracker.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final IssueService issueService;

    public ProjectServiceImpl(ProjectRepository projectRepository, IssueService issueService) {
        this.projectRepository = projectRepository;
        this.issueService = issueService;
    }


    @Override
    public List<Project> findByName(String name) {
        if (name == null) {
            name = "";
        }
        return projectRepository.findByNameContaining(name);
    }

    @Override
    public Project findById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    @Override
    @Transactional
    public Project create(Project project) {
        System.out.println("create project " + project );
        project.setId(0L);
        if (project.getKey() == null || project.getKey().isEmpty()) {
            System.out.println("ket is null " + project.getKey());
            project.setKey(project.generateKey(project.getName()));
        }
        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public Project update(Project project) {
        return projectRepository.save(project);
    }

    @Override
    @Transactional
    public void deleteById(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }
        List<Issue> issues = issueService.findByProjectId(projectId);
        for (Issue issue : issues) {
            issueService.deleteById(issue.getId());
        }
        projectRepository.deleteById(projectId);
    }


}
