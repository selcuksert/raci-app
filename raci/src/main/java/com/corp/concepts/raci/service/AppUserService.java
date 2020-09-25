package com.corp.concepts.raci.service;

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

	public AppUserService(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	public AppUser addUser(String username, String password, String role) {

		AppUser appUser = new AppUser();
		appUser.setUsername(username);
		appUser.setPassword(new BCryptPasswordEncoder().encode(password));
		appUser.setRole(Role.valueOf(role).name());

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

}
