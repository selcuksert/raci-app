package com.corp.concepts.raci.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.corp.concepts.raci.entity.Assignment;
import com.corp.concepts.raci.entity.Task;
import com.corp.concepts.raci.model.TaskToAssign;
import com.corp.concepts.raci.service.AssignmentService;
import com.corp.concepts.raci.service.StakeholderService;

@RestController
@RequestMapping("/task")
public class TaskController {

	private AssignmentService assignmentService;
	private StakeholderService stakeholderService;

	public TaskController(AssignmentService assignmentService, StakeholderService stakeholderService) {
		this.assignmentService = assignmentService;
		this.stakeholderService = stakeholderService;
	}

	@PostMapping
	public Assignment addTask(@RequestBody TaskToAssign taskToAssign) {
		try {
			Task task = assignmentService.addTask(taskToAssign.getStakeholderName(), taskToAssign.getTaskDetail(),
					taskToAssign.getResponsibilityName());

			return assignmentService
					.getAssignment(task, stakeholderService.findStakeholderByName(taskToAssign.getStakeholderName()));
		} catch (IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage(), iae);
		}
	}
}
