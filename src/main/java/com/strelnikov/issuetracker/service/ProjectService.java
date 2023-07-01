package com.strelnikov.issuetracker.service;


import com.strelnikov.issuetracker.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    Page<Project> findAll(Pageable pageable);

    Page<Project> findByName(String name, Pageable pageable);

    Project findById(Long id);

    Project update(Project project);

    void deleteById(Long projectId);

    Project create(Project project);

}
