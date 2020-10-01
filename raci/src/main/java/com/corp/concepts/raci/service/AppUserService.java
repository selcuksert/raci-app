package com.corp.concepts.raci.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.corp.concepts.raci.entity.AppUser;
import com.corp.concepts.raci.model.Messages;
import com.corp.concepts.raci.model.Role;
import com.corp.concepts.raci.repository.AppUserRepository;

@Service
public class AppUserService {

	private AppUserRepository appUserRepository;
	private RoleService roleService;
	private PasswordEncoder passwordEncoder;

	public AppUserService(AppUserRepository appUserRepository, RoleService roleService,
			PasswordEncoder passwordEncoder) {
		this.appUserRepository = appUserRepository;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
	}

	public AppUser addUser(String username, String password, String role) {

		AppUser appUser = new AppUser();
		appUser.setUsername(username);
		appUser.setPassword(passwordEncoder.encode(password));
		appUser.setRoles(roleService.getRoleContext(Role.valueOf(role.toUpperCase())));

		return appUserRepository.save(appUser);
	}

	public void changePassword(String oldPassword, String newPassword) {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		AppUser appUser = appUserRepository.findByUsername(username);

		Assert.isTrue(passwordEncoder.matches(oldPassword, appUser.getPassword()), Messages.Error.WRONG_OLD_PASSWORD);

		appUser.setPassword(passwordEncoder.encode(newPassword));

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

	private String generateRandomSpecialCharacters(int length) {
		RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(33, 45).build();
		return pwdGenerator.generate(length);
	}

	private String generateRandomAlphabet(int length, boolean lowerCase) {
		int low;
		int hi;
		if (lowerCase) {
			low = 97;
			hi = 122;
		} else {
			low = 65;
			hi = 90;
		}
		RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(low, hi).build();
		return pwdGenerator.generate(length);
	}

	private String generateRandomNumbers(int length) {
		RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(48, 57).build();
		return pwdGenerator.generate(length);
	}

	private String generateRandomCharacters(int length) {
		RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(48, 57).build();
		return pwdGenerator.generate(length);
	}

	public String generateRandomPassword() {
		String pwString = generateRandomSpecialCharacters(2).concat(generateRandomNumbers(2))
				.concat(generateRandomAlphabet(2, true)).concat(generateRandomAlphabet(2, false))
				.concat(generateRandomCharacters(2));
		List<Character> pwChars = pwString.chars().mapToObj(data -> (char) data).collect(Collectors.toList());
		Collections.shuffle(pwChars);
		String password = pwChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
				.toString();
		return password;
	}
}
