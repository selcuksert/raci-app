package com.corp.concepts.raci.repository;

import org.springframework.data.repository.CrudRepository;

import com.corp.concepts.raci.entity.Stakeholder;

public interface StakeholderRepository extends CrudRepository<Stakeholder, Long> {

	public Stakeholder findFirstByName(String name);
}
