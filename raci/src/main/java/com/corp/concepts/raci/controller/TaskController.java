package com.corp.concepts.raci.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.corp.concepts.raci.entity.Assignment;
import com.corp.concepts.raci.model.Messages;
import com.corp.concepts.raci.model.Response;
import com.corp.concepts.raci.model.StakeholderData;
import com.corp.concepts.raci.model.TaskToAssign;
import com.corp.concepts.raci.service.AssignmentService;

@RestController
@RequestMapping("/task")
public class TaskController {

	private AssignmentService assignmentService;

	public TaskController(AssignmentService assignmentService) {
		this.assignmentService = assignmentService;
	}

	@PostMapping
	public Response addTask(@RequestBody TaskToAssign taskToAssign) {
		try {
			List<StakeholderData> stakeholderData = taskToAssign.getStakeholderData();

			assignmentService.addTask(stakeholderData, taskToAssign.getTaskDetail(), taskToAssign.getAdditionalInfo());

			return new Response(true, Messages.Success.TASK_ADDED);
		} catch (IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage(), iae);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@GetMapping
	public List<Assignment> getAllTasks() {
		try {
			List<Assignment> assignments = assignmentService.getAllAssignments();
						
			assignments.stream().collect(Collectors.groupingBy(Assignment::getTask));
			
			return assignments;
		} catch (IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage(), iae);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}

