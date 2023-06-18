package com.strelnikov.issuetracker.service;

//import com.strelnikov.issuetracker.config.PlainAuthentication;

import com.strelnikov.issuetracker.entity.ProjectRole;
import com.strelnikov.issuetracker.entity.ProjectRoleType;
import com.strelnikov.issuetracker.entity.User;
import com.strelnikov.issuetracker.exception.UserAlreadyExistsException;
import com.strelnikov.issuetracker.exception.UserNotFoundException;
import com.strelnikov.issuetracker.repository.UserRepository;
import com.strelnikov.issuetracker.repository.UserRoleRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepository;
	private UserRoleRepository userRoleRepository;
	
	public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
	}

	public User getCurrentUser() {
		Jwt token  = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = token.getClaimAsString("sub");
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

//	private User getCurrentUser() {
//		final var userId = ((PlainAuthentication) SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
//		return userRepository.findById(userId).orElseThrow();
//	}

	@Override
	public List<User> findAll() {
		User user = getCurrentUser();
		// find all project IDs where user is ADMIN or MANAGER
		List<ProjectRole> projects = user.getProjectRoles();
		Set<Long> projectIds = projects.stream()
				.filter(role -> role.getType() != ProjectRoleType.VIEWER)
				.map(role -> role.getProject().getId())
				.collect(Collectors.toSet());
		Set<User> users = new HashSet<>();
		// add to list users with role VIEWER
		for (Long id : projectIds) {
			users.addAll(userRepository.findViewersByProjectId(id, ProjectRoleType.VIEWER));
		}
		return users.stream().toList();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
		return null;
	}

	@Override
	public User findByUsername(String username) {
		return null;
	}

	//	@Override
//	public User findByEmail(String email) {
//		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
//		return user;
//	}


	@Override
	public User save(User user) {
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new UserAlreadyExistsException(user.getUsername());
		}
		return userRepository.save(user);
	}

	@Override
	public User update(String username, User requestUser) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username));

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
	@Transactional
	public void deleteByUsername(String username) {
		Long userId = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(username))
				.getId();

		userRoleRepository.deleteAllByUserId(userId);
		userRepository.deleteByUsername(username);
	}
}
