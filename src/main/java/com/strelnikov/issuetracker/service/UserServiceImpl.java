package com.strelnikov.issuetracker.service;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
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
	public UserDetails loadUserByEmail(String email) throws UserNotFoundException {
		return null;
	}

	@Override
	public User findByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(email));
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
				.orElseThrow(() -> new UserNotFoundException(id.toString()));
//		if (!Objects.equals(user.getId(), getCurrentUser().getId())) {
//			throw new AccessForbiddenException();
//		}
		user.setFirstName(requestUser.getFirstName());
		user.setLastName(requestUser.getLastName());
		user.setCompanyName(requestUser.getCompanyName());
		user.setEmail(requestUser.getEmail());
		return userRepository.save(user);
	}

	// to implement
	@Override
	public void deleteById(Long userId) {
		userRoleRepository.deleteAllByUserId(userId);
		userRepository.deleteById(userId);
	}

	@Override
	@Transactional
	public void deleteByEmail(String email) {
		Long userId = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(email))
				.getId();
		userRoleRepository.deleteAllByUserId(userId);
		userRepository.deleteByEmail(email);
	}
}
