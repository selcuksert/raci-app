package com.corp.concepts.raci.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.corp.concepts.raci.entity.Stakeholder;
import com.corp.concepts.raci.model.Errors;
import com.corp.concepts.raci.repository.StakeholderRepository;

@Service
public class StakeholderService {

	private StakeholderRepository stakeholderRepository;

	public StakeholderService(StakeholderRepository stakeholderRepository) {
		this.stakeholderRepository = stakeholderRepository;
	}

	public Stakeholder addStakeholder(String name) {
		Assert.hasText(name, Errors.NO_NAME_SHOLDER);
		Stakeholder stakeholder = new Stakeholder();
		stakeholder.setName(name);

		return stakeholderRepository.save(stakeholder);
	}
	
	public Stakeholder findStakeholderByName(String name) {
		return stakeholderRepository.findFirstByName(name);
	}

}
