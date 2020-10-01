package com.corp.concepts.raci.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.corp.concepts.raci.model.Role;
import com.corp.concepts.raci.service.AppUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private AppUserDetailsService appUserDetailsService;

	public WebSecurityConfig(AppUserDetailsService appUserDetailsService) {
		this.appUserDetailsService = appUserDetailsService;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/styles/**", "/scripts/**", "/images/**", "/pages/**", "/components/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off		 
		http
		.csrf().disable()
		.authorizeRequests().antMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
		.and()
		.authorizeRequests().antMatchers("/user/**").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
		.and()
		.authorizeRequests().antMatchers("/**").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name(), Role.VIEW.name())
		.and()
		.httpBasic()
		.and()
		.formLogin().usernameParameter("username").passwordParameter("password")
		.loginPage("/login.html").permitAll()
		.and()
		.logout().permitAll();
		//@formatter:on
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
		//@formatter:off		 
		authenticationMgr
		.userDetailsService(appUserDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
		//@formatter:on
	}

}
