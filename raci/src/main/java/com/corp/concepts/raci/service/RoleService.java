package com.corp.concepts.raci.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.corp.concepts.raci.model.Role;

@Service
public class RoleService {

	public List<String> getRoleContext(Role role) {

		switch (role) {
		case ADMIN:
			return Stream.of(Role.values()).map(Enum::name).collect(Collectors.toList());
		case USER:
			return Arrays.asList(Role.USER.name(), Role.VIEW.name());
		case VIEW:
			return Arrays.asList(Role.VIEW.name());
		default:
			return null;
		}

	}
}
