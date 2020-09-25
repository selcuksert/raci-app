package com.corp.concepts.raci.repository;

import org.springframework.data.repository.CrudRepository;

import com.corp.concepts.raci.entity.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
	public AppUser findByUsername(String username);
}
