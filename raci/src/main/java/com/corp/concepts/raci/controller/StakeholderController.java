package com.corp.concepts.raci.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.corp.concepts.raci.entity.Stakeholder;
import com.corp.concepts.raci.service.StakeholderService;

@RestController
@RequestMapping("/stakeholder")
public class StakeholderController {

	private StakeholderService stakeholderService;

	public StakeholderController(StakeholderService stakeholderService) {
		this.stakeholderService = stakeholderService;
	}

	@PostMapping
	public Stakeholder addStakeholder(@RequestParam String name) {
		try {
			return stakeholderService.addStakeholder(name);
		} catch (IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage(), iae);
		}
	}
}
