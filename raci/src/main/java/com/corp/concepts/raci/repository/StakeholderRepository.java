package com.corp.concepts.raci.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.corp.concepts.raci.entity.Stakeholder;

public interface StakeholderRepository extends CrudRepository<Stakeholder, Long> {

	public Stakeholder findFirstByName(String name);
	
	public Iterable<Stakeholder> findAllByOrderByNameAsc();

	@Query(value = "SELECT name FROM stakeholder", nativeQuery = true)
	public List<String> findAllByName();

}
