package com.corp.concepts.raci.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.corp.concepts.raci.model.TaskList;
import com.corp.concepts.raci.model.TaskToView;
import com.corp.concepts.raci.repository.AssignmentRepository;
import com.corp.concepts.raci.repository.ResponsibilityRepository;
import com.corp.concepts.raci.repository.StakeholderRepository;
import com.corp.concepts.raci.repository.TaskRepository;

@Service
public class AssignmentService {

	@PersistenceContext
	protected EntityManager entityManager;

	private ResponsibilityRepository responsibilityRepository;
	private StakeholderRepository stakeholderRepository;
	private AssignmentRepository assignmentRepository;
	private TaskRepository taskRepository;

	public AssignmentService(ResponsibilityRepository responsibilityRepository,
			StakeholderRepository stakeholderRepository, AssignmentRepository assignmentRepository,
			TaskRepository taskRepository) {
		this.responsibilityRepository = responsibilityRepository;
		this.stakeholderRepository = stakeholderRepository;
		this.assignmentRepository = assignmentRepository;
		this.taskRepository = taskRepository;
	}

	@Transactional
	public void addTask(List<StakeholderData> stakeholderData, String taskDetail, String additionalInfo) {

		Assert.isNull(taskRepository.findFirstByDetail(taskDetail), Messages.Error.TASK_EXISTS);

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

	private List<Assignment> getAllAssignments() {
		Iterable<Assignment> assignmentDbData = assignmentRepository.findAllByOrderByTaskAsc();
		List<Assignment> assignmentData = new ArrayList<>();

		assignmentDbData.forEach(assignmentData::add);

		Assert.notEmpty(assignmentData, Messages.Error.EMPTY_ASSIGNMENT);

		return assignmentData;
	}

	public TaskList getAllTasks() {

		List<TaskToView> tasks = new ArrayList<>();
		List<Assignment> assignments = getAllAssignments();
		List<String> stakeholders = stakeholderRepository.findAllByName();

		assignments.stream().forEach(assignment -> {
			TaskToView taskToView = null;
			if (!tasks.stream().anyMatch(t -> t.getId() == assignment.getTask().getId())) {
				taskToView = new TaskToView();
				taskToView.setId(assignment.getTask().getId());
				List<String> responsibilities = new ArrayList<>();
				stakeholders.stream().forEach(s -> responsibilities.add("-"));
				taskToView.setResponsibilities(responsibilities);
				taskToView.setTaskDescription(assignment.getTask().getDetail());
				taskToView.setAdditionalInfo(assignment.getAdditionalInfo());
				tasks.add(taskToView);
			}

			taskToView = tasks.stream().filter(t -> t.getId() == assignment.getTask().getId()).findFirst().get();

			List<Responsibility> responsibilities = assignment.getResponsibilities();

			String respText = responsibilities.stream().map(r -> r.getName()).collect(Collectors.joining(", "));

			taskToView.getResponsibilities().set(stakeholders.indexOf(assignment.getStakeholder().getName()), respText);
		});

		TaskList taskList = new TaskList();
		taskList.setStakeholders(stakeholders);
		taskList.setTasks(tasks);
		
		return taskList;
	}

	private void deleteAssignmentById(Long taskId, Long stakeholderId) {
		AssignmentKey id = new AssignmentKey();
		id.setTaskId(taskId);
		id.setStakeholderId(stakeholderId);

		assignmentRepository.deleteById(id);
	}
	
	public void deleteAllAssignmentsByTaskId(Long taskId) {
		List<Long> stakeholders = assignmentRepository.findAllStakeholdersByTaskId(taskId);
		
		stakeholders.stream().forEach(stakeholderId -> {
			deleteAssignmentById(taskId, stakeholderId);
		});
		
		taskRepository.deleteById(taskId);
	}

}
