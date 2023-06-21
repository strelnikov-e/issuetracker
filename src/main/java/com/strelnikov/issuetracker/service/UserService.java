package com.strelnikov.issuetracker.service;


import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    User getCurrentUser();

    UserDetails loadUserByEmail(String email) throws UserNotFoundException;
	
	User findByEmail(String email);

    List<User> findAll();

    User save(User user);

    User update(Long id, User user);

    void delete();

    User getUserDetails();
}
