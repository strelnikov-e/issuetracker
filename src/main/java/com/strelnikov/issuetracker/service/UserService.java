package com.strelnikov.issuetracker.service;


import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    UserDetails loadUserByUsername(String username) throws UserNotFoundException;
	
	User findByUsername(String username);

    List<User> findAll();

    void deleteByUsername(String username);

    User save(User user);

    User update(String username, User user);
}
