package com.example.demo.configurations;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

public class SpringConfig {
	public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
		
		
		 return http.csrf().disable().authorizeHttpRequests()
				 .requestMatchers("/employees/add").permitAll()
//				 .and()
//				  .authorizeHttpRequests()
				 .requestMatchers("/employees").authenticated()
				 .and().formLogin()
				 .and().build();

	   }

}
