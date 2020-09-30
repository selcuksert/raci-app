package com.corp.concepts.raci.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.corp.concepts.raci.entity.AppUser;
import com.corp.concepts.raci.model.Role;
import com.corp.concepts.raci.repository.AppUserRepository;

@Service
public class AppUserService {

	private AppUserRepository appUserRepository;
	private RoleService roleService;

	public AppUserService(AppUserRepository appUserRepository, RoleService roleService) {
		this.appUserRepository = appUserRepository;
		this.roleService = roleService;
	}

	public AppUser addUser(String username, String password, String role) {

		AppUser appUser = new AppUser();
		appUser.setUsername(username);
		appUser.setPassword(new BCryptPasswordEncoder().encode(password));
		appUser.setRoles(roleService.getRoleContext(Role.valueOf(role.toUpperCase())));

		return appUserRepository.save(appUser);
	}

	public void changePassword(String newPassword) {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		AppUser appUser = appUserRepository.findByUsername(username);
		appUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));

		appUserRepository.save(appUser);
	}

	public List<String> getUserRoles() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<String> roles = null;
		if (principal instanceof UserDetails) {
			roles = ((UserDetails) principal).getAuthorities().stream().map(auth -> auth.getAuthority())
					.collect(Collectors.toList());
		}

		return roles;
	}
}
