package com.corp.concepts.raci.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.corp.concepts.raci.entity.AppUser;
import com.corp.concepts.raci.model.AppUserToAdd;
import com.corp.concepts.raci.model.Messages;
import com.corp.concepts.raci.model.PasswordToChange;
import com.corp.concepts.raci.model.Response;
import com.corp.concepts.raci.service.AppUserService;

@RestController
@RequestMapping
public class AppUserController {

	private AppUserService appUserService;

	public AppUserController(AppUserService appUserService) {
		this.appUserService = appUserService;
	}

	@PostMapping("/admin/user")
	public Response addUser(@RequestBody AppUserToAdd appUserToAdd) {
		try {
			AppUser appUser = appUserService.addUser(appUserToAdd.getUsername(), appUserToAdd.getPassword(),
					appUserToAdd.getRole());

			return new Response(true, String.format(Messages.Success.USER_ADDED, appUser.getUsername()));
		} catch (IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage(), iae);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@PatchMapping("/password")
	public Response changePassword(@RequestBody PasswordToChange passwordToChange) {
		try {
			appUserService.changePassword(passwordToChange.getNewPassword());

			return new Response(true, Messages.Success.PASSWORD_CHANGED);
		} catch (IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage(), iae);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@GetMapping("/roles")
	public List<String> getUserRoles() {
		try {
			return appUserService.getUserRoles();
		} catch (IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage(), iae);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
