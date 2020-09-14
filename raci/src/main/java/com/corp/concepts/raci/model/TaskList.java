package com.corp.concepts.raci.model;

import java.util.List;

import lombok.Data;

@Data
public class TaskList {

	private List<String> stakeholders;
	private List<TaskToView> tasks;
}
