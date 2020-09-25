package com.corp.concepts.raci.start;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.corp.concepts.raci.entity.AppUser;
import com.corp.concepts.raci.entity.Responsibility;
import com.corp.concepts.raci.model.Role;
import com.corp.concepts.raci.repository.AppUserRepository;
import com.corp.concepts.raci.repository.ResponsibilityRepository;

@Component
public class AppStartRunner implements ApplicationRunner {

	private ResponsibilityRepository responsibilityRepository;
	private AppUserRepository appUserRepository;

	@Value("${custom.property.security.admin.username}")
	private String adminUsername;
	@Value("${custom.property.security.admin.password}")
	private String adminPassword;

	public AppStartRunner(ResponsibilityRepository responsibilityRepository, AppUserRepository appUserRepository) {
		this.responsibilityRepository = responsibilityRepository;
		this.appUserRepository = appUserRepository;
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

	private void addAdminUser() {
		AppUser appUser = appUserRepository.findByUsername(adminUsername);

		if (appUser == null) {
			appUser = new AppUser();
			appUser.setUsername(adminUsername);
			appUser.setPassword(new BCryptPasswordEncoder().encode(adminPassword));
			appUser.setRole(Role.ADMIN.name());
			appUserRepository.save(appUser);
		}

	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		addResponsibilityBaseData();
		addAdminUser();
	}

}
