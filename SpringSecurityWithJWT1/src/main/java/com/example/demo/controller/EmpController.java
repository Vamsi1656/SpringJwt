package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AuthRequest;
import com.example.demo.entity.Employee;
import com.example.demo.repository.EmpRepository;
import com.example.demo.service.JwtService;

@RestController
public class EmpController {
	
	@Autowired
	EmpRepository repo;
	
	@Autowired
	private JwtService jwtservice;
	
	@PostMapping("/employees")
	public List<Employee> createEmployee(@RequestBody List<Employee> emp){
		
		List<Employee> employee=repo.saveAll(emp);
		
		return employee;
	}
	
	@GetMapping("/employees")
	public List<Employee> getAllEmployee(){
		
		List<Employee> employee=repo.findAll();
		
		return employee;
	}

	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		return jwtservice.generateJwtToken(authRequest.getUsername());
		
	}

}
