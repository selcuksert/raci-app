package com.corp.concepts.raci.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.corp.concepts.raci.entity.Assignment;
import com.corp.concepts.raci.entity.Responsibility;
import com.corp.concepts.raci.entity.Stakeholder;
import com.corp.concepts.raci.entity.Task;
import com.corp.concepts.raci.entity.key.AssignmentKey;
import com.corp.concepts.raci.model.Errors;
import com.corp.concepts.raci.repository.AssignmentRepository;
import com.corp.concepts.raci.repository.ResponsibilityRepository;
import com.corp.concepts.raci.repository.StakeholderRepository;

@Service
public class AssignmentService {

	@PersistenceContext
	protected EntityManager entityManager;

	private ResponsibilityRepository responsibilityRepository;
	private StakeholderRepository stakeholderRepository;
	private AssignmentRepository assignmentRepository;

	public AssignmentService(ResponsibilityRepository responsibilityRepository,
			StakeholderRepository stakeholderRepository, AssignmentRepository assignmentRepository) {
		this.responsibilityRepository = responsibilityRepository;
		this.stakeholderRepository = stakeholderRepository;
		this.assignmentRepository = assignmentRepository;
	}

	@Transactional
	public Task addTask(String stakeholderName, String taskDetail, String responsibilityName) {
		Stakeholder stakeholder = stakeholderRepository.findFirstByName(stakeholderName);
		Assert.notNull(stakeholder, Errors.UND_STAKEHOLDER);

		Responsibility responsibility = responsibilityRepository.findFirstByName(responsibilityName);
		Assert.notNull(responsibility, Errors.UND_RESPONSIBILITY);

		entityManager.persist(stakeholder);

		Task task = new Task();
		task.setDetail(taskDetail);
		entityManager.persist(task);

		Assignment assignment = new Assignment();
		assignment.setId(new AssignmentKey());
		assignment.setStakeholder(stakeholder);
		assignment.setTask(task);
		assignment.setResponsibility(responsibility);
		entityManager.persist(assignment);

		return task;
	}

	public Assignment getAssignment(Task task, Stakeholder stakeholder) {
		return assignmentRepository.findFirstByTaskAndStakeholder(task, stakeholder);
	}

}
