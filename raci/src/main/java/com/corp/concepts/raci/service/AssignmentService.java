package com.corp.concepts.raci.service;

import java.util.ArrayList;
import java.util.List;

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
import com.corp.concepts.raci.model.Messages;
import com.corp.concepts.raci.model.StakeholderData;
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
	public void addTask(List<StakeholderData> stakeholderData, String taskDetail,
			String additionalInfo) {
		
		Task task = new Task();
		task.setDetail(taskDetail);
		entityManager.persist(task);

		stakeholderData.stream().forEach(stk -> {
			Stakeholder stakeholder = stakeholderRepository.findFirstByName(stk.getName());
			Assert.notNull(stakeholder, String.format(Messages.Error.UND_STAKEHOLDER, stk.getName()));
			Assert.notEmpty(stk.getResponsibilityNames(), String.format(Messages.Error.UND_RESPONSIBILITY, "empty"));

			List<Responsibility> responsibilities = new ArrayList<>();

			stk.getResponsibilityNames().stream().forEach(name -> {
				Responsibility responsibility = responsibilityRepository.findFirstByName(name);
				Assert.notNull(responsibility, String.format(Messages.Error.UND_RESPONSIBILITY, name));
				responsibilities.add(responsibility);
			});

			entityManager.persist(stakeholder);
			
			Assignment assignment = new Assignment();
			assignment.setId(new AssignmentKey());
			assignment.setStakeholder(stakeholder);
			assignment.setTask(task);
			assignment.setResponsibilities(responsibilities);
			assignment.setAdditionalInfo(additionalInfo);
			entityManager.persist(assignment);
		});
	}

	public Assignment getAssignment(Task task, Stakeholder stakeholder) {
		return assignmentRepository.findFirstByTaskAndStakeholder(task, stakeholder);
	}

	public List<Assignment> getAllAssignments() {
		Iterable<Assignment> assignmentDbData = assignmentRepository.findAll();
		List<Assignment> assignmentData = new ArrayList<>();

		assignmentDbData.forEach(assignmentData::add);

		Assert.notEmpty(assignmentData, Messages.Error.EMPTY_ASSIGNMENT);

		return assignmentData;
	}

	public void deleteAssignment(Long taskId, Long stakeholderId) {
		AssignmentKey id = new AssignmentKey();
		id.setTaskId(taskId);
		id.setStakeholderId(stakeholderId);

		assignmentRepository.deleteById(id);
	}

}
