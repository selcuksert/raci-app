package com.corp.concepts.raci.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {

	private boolean success;
	private String message;
}
