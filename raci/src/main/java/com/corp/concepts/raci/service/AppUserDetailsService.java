package com.corp.concepts.raci.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.corp.concepts.raci.entity.AppUser;
import com.corp.concepts.raci.repository.AppUserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

	private AppUserRepository appUserRepository;

	public AppUserDetailsService(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = appUserRepository.findByUsername(username);

		if (appUser == null) {
			throw new UsernameNotFoundException(username + " not found!");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();

		List<String> roles = appUser.getRoles();

		roles.stream().forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

		User user = new User(appUser.getUsername(), appUser.getPassword(), authorities);

		return user;
	}

}
