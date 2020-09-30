package com.corp.concepts.raci.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.corp.concepts.raci.entity.Stakeholder;
import com.corp.concepts.raci.model.StakeholderToAdd;
import com.corp.concepts.raci.service.StakeholderService;

@RestController
@RequestMapping
public class StakeholderController {

	private StakeholderService stakeholderService;

	public StakeholderController(StakeholderService stakeholderService) {
		this.stakeholderService = stakeholderService;
	}

	@PostMapping("/admin/stakeholder")
	public Stakeholder addStakeholder(@RequestBody StakeholderToAdd stakeholder) {
		try {
			return stakeholderService.addStakeholder(stakeholder.getName());
		} catch (IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage(), iae);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
	
	@GetMapping("/stakeholders")
	public List<Stakeholder> getStakeholders() {
		try {
			return stakeholderService.getAllStakeholders();
		} catch (IllegalArgumentException iae) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, iae.getMessage(), iae);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
