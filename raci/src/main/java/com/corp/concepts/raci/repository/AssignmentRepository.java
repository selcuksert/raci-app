package com.corp.concepts.raci.repository;

import org.springframework.data.repository.CrudRepository;

import com.corp.concepts.raci.entity.Assignment;
import com.corp.concepts.raci.entity.Stakeholder;
import com.corp.concepts.raci.entity.Task;
import com.corp.concepts.raci.entity.key.AssignmentKey;

public interface AssignmentRepository extends CrudRepository<Assignment, AssignmentKey> {

	public Assignment findFirstByTaskAndStakeholder(Task task, Stakeholder stakeholder);
	
	public Iterable<Assignment> findAllByOrderByTaskAsc();

}
