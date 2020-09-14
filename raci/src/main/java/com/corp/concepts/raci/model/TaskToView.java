package com.corp.concepts.raci.model;

import java.util.List;

import lombok.Data;

@Data
public class TaskToView {
	
	private Long id;
	private String taskDescription;
	private String additionalInfo;
	private List<String> responsibilities;
}
