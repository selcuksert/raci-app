package com.corp.concepts.raci.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/styles/**", "/scripts/**", "/images/**", "/pages/**", "/components/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off		 
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/**").hasAnyRole("USER", "ADMIN").and()
		.formLogin().usernameParameter("username").passwordParameter("password")
		.loginPage("/login.html").permitAll().and()
		.logout().permitAll();
		//@formatter:on

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
		//@formatter:off		 
		authenticationMgr.inMemoryAuthentication()
		.withUser("employee").password("{noop}employee").roles("USER").and()
		.withUser("javainuse").password("{noop}javainuse").roles("USER", "ADMIN");
		//@formatter:on
	}

}
