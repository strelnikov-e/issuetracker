package com.strelnikov.issuetracker.service;


import com.strelnikov.issuetracker.entity.IssueRoleType;
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

    User save(User user);

    User update(Long id, User user);

    void delete();

    User getUserDetails();

    User findByIssueRole(Long issueId, IssueRoleType assignee);

    User patch(Map<String, Object> fields);

}
