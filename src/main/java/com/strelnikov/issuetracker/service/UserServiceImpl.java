package com.strelnikov.issuetracker.service;

import com.strelnikov.issuetracker.entity.IssueRoleType;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.exception.CannotProcessRequest;
import com.strelnikov.issuetracker.exception.UserAlreadyExistsException;
import com.strelnikov.issuetracker.exception.UserNotFoundException;
import com.strelnikov.issuetracker.repository.UserRepository;
import com.strelnikov.issuetracker.repository.UserRoleRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User getCurrentUser() {
		Jwt token  = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = token.getClaimAsString("sub");
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	/*
    Returns list of users assigned to the projects, where current user is MANAGER or ADMIN
     */
	@Override
	public Set<User> findAll() {
		User user = userRepository.findById(getCurrentUser().getId())
				.orElseThrow(UserNotFoundException::new);
		Set<Long> projectIds = getProjectIdsOfUser(user);
		Set<User> users = new HashSet<>();
		for (Long id : projectIds) {
			users.addAll(userRepository.findByProjectId(id));
		}
		return users;
	}

	@Override
	public UserDetails loadUserByEmail(String email) throws UserNotFoundException {
		return null;
	}

	@Override
	public User findByIssueRole(Long issueId, IssueRoleType role) {
		return userRepository.findByIssueRole(issueId ,role).orElseGet(User::new);
	}

	@Override
	public User findByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException());
		return user;
	}

	@Override
	public User save(User requestUser) {
		if (userRepository.existsByEmail(requestUser.getEmail())) {
			throw new UserAlreadyExistsException(requestUser.getEmail());
		}
		final String encodedPwd = "{bcrypt}" + passwordEncoder.encode(requestUser.getPassword());
		requestUser.setPassword(encodedPwd);
		System.out.println(requestUser.getPassword());
		return userRepository.save(requestUser);
	}

	@Override
	public User update(Long id, User requestUser) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException());
//		if (!Objects.equals(user.getId(), getCurrentUser().getId())) {
//			throw new AccessForbiddenException();
//		}
		user.setFirstName(requestUser.getFirstName());
		user.setLastName(requestUser.getLastName());
		user.setCompanyName(requestUser.getCompanyName());
		user.setEmail(requestUser.getEmail());
		return userRepository.save(user);
	}

	@Override
	public User patch(Map<String, Object> fields) {
		User user = userRepository.findById(getCurrentUser().getId())
				.orElseThrow(UserNotFoundException::new);
		fields.forEach((k, v) -> {
			switch (k) {
				case "firstName" -> user.setFirstName(v.toString());
				case "lastName" -> user.setLastName(v.toString());
				case "companyName" -> user.setCompanyName(v.toString());
				case "currentProject" -> user.setCurrentProject(Long.parseLong(v.toString()));
				}
		});
		return userRepository.save(user);
	}

	@Override
	public User getUserDetails() {
		Long id = getCurrentUser().getId();
		return userRepository.findById(id)
				.orElseThrow(UserNotFoundException::new);
	}


	@Override
	@Transactional
	public void delete() {
		User user = userRepository.findById(getCurrentUser().getId())
				.orElseThrow(UserNotFoundException::new);
		if (!getProjectIdsOfUser(user).isEmpty()) {
			throw new CannotProcessRequest("Unable to process the request. " +
					"User has Admin or Manager assignments on the project(s)");
		}
		if (!getIssueIdsOfUser(user).isEmpty()) {
			throw new CannotProcessRequest("Unable to process the request. " +
					"User has assignments on the issue(s)");
		}
		userRoleRepository.deleteAllByUserId(user.getId());
		userRepository.deleteById(user.getId());
	}

	private Set<Long> getIssueIdsOfUser(User user) {
		return user.getIssueRoles().stream()
				.filter(role -> role.getType() == IssueRoleType.ASSIGNEE)
				.map(role -> role.getIssue().getId())
				.collect(Collectors.toSet());
	}

	// find all project IDs where user is ADMIN or MANAGER
	private Set<Long> getProjectIdsOfUser(User user) {
		return user.getProjectRoles().stream()
				.filter(role -> role.getType() != ProjectRoleType.VIEWER)
				.map(role -> role.getProject().getId())
				.collect(Collectors.toSet());
	}
}
