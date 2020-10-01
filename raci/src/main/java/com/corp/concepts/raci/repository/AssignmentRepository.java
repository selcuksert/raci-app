package com.corp.concepts.raci.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.corp.concepts.raci.entity.Assignment;
import com.corp.concepts.raci.entity.Stakeholder;
import com.corp.concepts.raci.entity.Task;
import com.corp.concepts.raci.entity.key.AssignmentKey;

public interface AssignmentRepository extends CrudRepository<Assignment, AssignmentKey> {

	public Assignment findFirstByTaskAndStakeholder(Task task, Stakeholder stakeholder);

	public Iterable<Assignment> findAllByOrderByTaskAsc();

	@Query(value = "SELECT stakeholder_id FROM assignment a WHERE a.task_id = ?1", nativeQuery = true)
	public List<Long> findAllStakeholdersByTaskId(Long taskId);

}
