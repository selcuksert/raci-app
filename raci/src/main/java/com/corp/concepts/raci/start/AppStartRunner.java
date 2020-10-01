package com.corp.concepts.raci.start;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.corp.concepts.raci.entity.AppUser;
import com.corp.concepts.raci.entity.Responsibility;
import com.corp.concepts.raci.model.Role;
import com.corp.concepts.raci.repository.AppUserRepository;
import com.corp.concepts.raci.repository.ResponsibilityRepository;
import com.corp.concepts.raci.service.AppUserService;
import com.corp.concepts.raci.service.RoleService;

@Component
public class AppStartRunner implements ApplicationRunner {

	private ResponsibilityRepository responsibilityRepository;
	private AppUserRepository appUserRepository;
	private RoleService roleService;
	private AppUserService appUserService;
	private PasswordEncoder passwordEncoder;

	@Value("${custom.property.security.admin.username}")
	private String adminUsername;

	public AppStartRunner(ResponsibilityRepository responsibilityRepository, AppUserRepository appUserRepository,
			RoleService roleService, AppUserService appUserService, PasswordEncoder passwordEncoder) {
		this.responsibilityRepository = responsibilityRepository;
		this.appUserRepository = appUserRepository;
		this.roleService = roleService;
		this.appUserService = appUserService;
		this.passwordEncoder = passwordEncoder;
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
			String adminPassword = appUserService.generateRandomPassword();
			System.out.println("-> " + adminPassword);
			appUser.setPassword(passwordEncoder.encode(adminPassword));

			appUser.setRoles(roleService.getRoleContext(Role.ADMIN));

			appUserRepository.save(appUser);
		}

	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		addResponsibilityBaseData();
		addAdminUser();
	}

}
