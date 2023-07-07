package com.strelnikov.issuetracker.service;


import com.strelnikov.issuetracker.entity.IssueRoleType;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {

    User getCurrentUser();

    UserDetails loadUserByEmail(String email) throws UserNotFoundException;
	
	User findByEmail(String email);

    Set<User> findAll();

    List<User> findByProjectId(Long projectId);

    List<User> findByProjectIdAndIssueRole(long projectId, IssueRoleType issueRole);

    User save(User user);

    User update(Long id, User user);

    void delete();

    User getUserDetails();

    User patch(Map<String, Object> fields);

    User findByIssueRole(Long issueId, IssueRoleType assignee);

    User findByProjectRole(Long id, ProjectRoleType manager);

    boolean existById(Long id);

    User findById(Long id);
}
