package com.corp.concepts.raci.model;

import lombok.Data;

@Data
public class AppUserToAdd {
	private String username;
	private String password;
	private String role;
}
