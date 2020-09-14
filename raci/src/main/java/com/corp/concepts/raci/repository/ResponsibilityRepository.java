package com.corp.concepts.raci.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.corp.concepts.raci.entity.Responsibility;

public interface ResponsibilityRepository extends CrudRepository<Responsibility, Long> {

	public boolean existsResponsibilityByName(String name);

	public Responsibility findFirstByName(String name);

	@Query(value = "SELECT name FROM responsibility", nativeQuery = true)
	public List<String> findAllByName();
}
