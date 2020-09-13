package com.corp.concepts.raci.start;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.corp.concepts.raci.entity.Responsibility;
import com.corp.concepts.raci.repository.ResponsibilityRepository;
import com.corp.concepts.raci.service.AssignmentService;

@Component
public class AppStartRunner implements ApplicationRunner {

	private ResponsibilityRepository responsibilityRepository;

	public AppStartRunner(ResponsibilityRepository responsibilityRepository, AssignmentService assignmentService) {
		this.responsibilityRepository = responsibilityRepository;
	}

	private void addResponsibilityBaseData() {
		if (!responsibilityRepository.existsResponsibilityByName("R")) {
			Responsibility respR = new Responsibility();
			respR.setName("R");
			respR.setDetail("Responsible");
			responsibilityRepository.save(respR);
		}

		if (!responsibilityRepository.existsResponsibilityByName("A")) {
			Responsibility respA = new Responsibility();
			respA.setName("A");
			respA.setDetail("Accountable");
			responsibilityRepository.save(respA);
		}

		if (!responsibilityRepository.existsResponsibilityByName("C")) {
			Responsibility respC = new Responsibility();
			respC.setName("C");
			respC.setDetail("Consulted");
			responsibilityRepository.save(respC);
		}

		if (!responsibilityRepository.existsResponsibilityByName("I")) {
			Responsibility respI = new Responsibility();
			respI.setName("I");
			respI.setDetail("Informed");
			responsibilityRepository.save(respI);
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		addResponsibilityBaseData();
	}

}
