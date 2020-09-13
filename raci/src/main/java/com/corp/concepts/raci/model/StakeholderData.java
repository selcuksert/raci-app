package com.corp.concepts.raci.model;

import java.util.List;

import lombok.Data;

@Data
public class StakeholderData {
	private String name;
	private List<String> responsibilityNames;
}
