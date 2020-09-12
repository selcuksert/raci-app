package com.corp.concepts.raci.repository;

import org.springframework.data.repository.CrudRepository;

import com.corp.concepts.raci.entity.Responsibility;

public interface ResponsibilityRepository extends CrudRepository<Responsibility, Long> {

	public boolean existsResponsibilityByName(String name);

	public Responsibility findFirstByName(String name);
}
