package com.corp.concepts.raci.model;

import lombok.Data;

@Data
public class PasswordToChange {
	private String oldPassword;
	private String newPassword;
}
