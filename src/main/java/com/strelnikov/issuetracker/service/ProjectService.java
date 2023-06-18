package com.strelnikov.issuetracker.service;


import com.strelnikov.issuetracker.entity.Project;

import java.util.List;

public interface ProjectService {

    List<Project> findByName(String name);

    Project update(Project project);

    Project findById(Long id);

    void deleteById(Long projectId);

    Project create(Project project);

    List<Project> findAll();

//    Collection<Project> findByManager(long parseLong);
}
