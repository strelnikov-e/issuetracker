package com.strelnikov.issuetracker.service;


import com.strelnikov.issuetracker.controller.hateoas.ProjectModel;
import com.strelnikov.issuetracker.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    Page<Project> findAll(Pageable pageable);

    Page<Project> findByName(String name, Pageable pageable);

    Project findById(Long id);

    Project update(ProjectModel project);

    void deleteById(Long projectId);

    Project create(ProjectModel project);

    boolean existById(Long id);
}
