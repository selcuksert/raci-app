package com.corp.concepts.raci.model;

import java.util.List;

import lombok.Data;

@Data
public class TaskToAssign {
	
	String stakeholderName;
	String taskDetail;
	List<String> responsibilityNames;
}
