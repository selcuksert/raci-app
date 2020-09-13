package com.corp.concepts.raci.model;

import java.util.List;

import lombok.Data;

@Data
public class TaskToAssign {
	String taskDetail;
	String additionalInfo;

	List<StakeholderData> stakeholderData;
}
